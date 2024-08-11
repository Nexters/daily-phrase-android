package com.silvertown.android.dailyphrase.presentation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.os.bundleOf
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.silvertown.android.dailyphrase.domain.model.Result
import com.silvertown.android.dailyphrase.presentation.component.LoadingDialog
import com.silvertown.android.dailyphrase.presentation.component.TwoButtonBottomSheet
import com.silvertown.android.dailyphrase.presentation.component.WelcomeEventModal
import com.silvertown.android.dailyphrase.presentation.databinding.ActivityMainBinding
import com.silvertown.android.dailyphrase.presentation.util.LoginResultListener
import com.silvertown.android.dailyphrase.presentation.util.Constants.PHRASE_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel by viewModels<MainViewModel>()
    private var loginResultListener: LoginResultListener? = null
    private lateinit var loadingDialog: LoadingDialog

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            handlePermissionsResult(permissions)
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermissions()

        navController =
            (supportFragmentManager.findFragmentById(R.id.fcv_nav_host) as NavHostFragment).navController

        redirectToDetailOnMessageReceived()

        loadingDialog = LoadingDialog(this)

        initObserve()
        initComposeView()
        setFragmentResultListeners()
    }

    fun setLoginResultListener(
        listener: LoginResultListener,
    ) {
        this.loginResultListener = listener
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        navController.handleDeepLink(intent)
    }

    private fun initObserve() {
        lifecycleScope.launch {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    MainViewModel.UiEvent.ForceUpdate -> {
                        // TODO JH 강제 업데이트 기능 구현
                    }

                    is MainViewModel.UiEvent.NeedUpdate -> {
                        navController.navigate(
                            resId = R.id.action_global_twoButtonBottomSheet,
                            args = bundleOf(
                                "twoButtonBottomSheetArg" to TwoButtonBottomSheet.TwoButtonBottomSheetArg(
                                    imageUrl = event.imageUrl,
                                    leftButtonMessage = event.leftButtonMessage,
                                    rightButtonMessage = event.rightButtonMessage,
                                    requestKey = REQUEST_KEY_MOVE_TO_UPDATE,
                                ),
                            ),
                        )
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.loginState.collectLatest { state ->
                if (state.isLoggedIn) {
                    viewModel.updateSharedCount()
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    private fun initComposeView() {
        binding.composeView.setContent {
            val loginState by viewModel.loginState.collectAsStateWithLifecycle()
            val welcomeEventState by viewModel.welcomeEventState.collectAsStateWithLifecycle(
                initialValue = Pair(Result.Loading, true)
            )
            var shouldShowWelcome by remember { mutableStateOf(viewModel.shouldShowWelcomeModal) }

            when (val state = welcomeEventState.first) {
                is Result.Loading,
                is Result.Failure,
                is Result.Empty,
                -> Unit

                is Result.Success -> {
                    val prizeInfo = state.data
                    val pageCount = prizeInfo.total * 100 // 굳이 Int.MAX로 잡지 않음.
                    val pagerState = rememberPagerState(
                        initialPage = pageCount / 2,
                        pageCount = { pageCount }
                    )

                    // 비로그인이면서 이벤트가 진행 중일 때만 웰컴모달이 보여야 함.
                    if (shouldShowWelcome && !loginState.isLoggedIn && !welcomeEventState.second) {
                        WelcomeEventModal(
                            onDismissRequest = {
                                shouldShowWelcome = false
                                viewModel.updateWelcomeModalShown()
                            },
                            pagerState = pagerState,
                            onClickKaKaoLogin = {
                                kakaoLogin(targetPage = LoginResultListener.TargetPage.EVENT)
                                shouldShowWelcome = false
                                viewModel.updateWelcomeModalShown()
                            },
                            prizeInfo = prizeInfo.items
                        )
                    }
                }
            }
        }
    }

    private fun setFragmentResultListeners() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.fcvNavHost.id) as NavHostFragment
        navHostFragment.childFragmentManager.setFragmentResultListener(
            REQUEST_KEY_MOVE_TO_UPDATE,
            this
        ) { _, _ ->
            moveToUpdate()
        }
    }

    private fun moveToUpdate() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
        try {
            startActivity(intent)
        } catch (e: Exception) {
            val webIntent =
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                )
            if (webIntent.resolveActivity(packageManager) != null) {
                startActivity(webIntent)
            }
        }
    }

    fun kakaoLogin(targetPage: LoginResultListener.TargetPage? = null) {
        lifecycleScope.launch {
            kotlin.runCatching {
                loadingDialog.show()
                loginWithKakaoOrThrow(this@MainActivity)
            }.onSuccess { oAuthToken ->
                onSuccessKaKaoLogin(oAuthToken, targetPage)
                loadingDialog.dismiss()
            }.onFailure { throwable ->
                loadingDialog.dismiss()
                if (throwable is ClientError && throwable.reason == ClientErrorCause.Cancelled) {
                    Timber.d("사용자가 명시적으로 카카오 로그인 취소")
                } else {
                    Toast.makeText(this@MainActivity, "로그인에 실패했어요.", Toast.LENGTH_SHORT).show()
                    Timber.e(throwable, "로그인 실패 : ${throwable.message}")
                }
            }
        }
    }

    private suspend fun loginWithKakaoOrThrow(context: Context): OAuthToken {
        return if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            try {
                loginWithKakaoTalk(context)
            } catch (error: Throwable) {
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) throw error

                loginWithKakaoAccount(context)
            }
        } else {
            loginWithKakaoAccount(context)
        }
    }

    private suspend fun loginWithKakaoTalk(context: Context): OAuthToken {
        return suspendCancellableCoroutine { continuation ->
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                when {
                    error != null -> {
                        continuation.resumeWithException(error)
                    }

                    token != null -> {
                        continuation.resume(token)
                    }

                    else -> {
                        continuation.resumeWithException(RuntimeException("kakao access token 받기 실패"))
                    }
                }
            }
        }
    }

    private suspend fun loginWithKakaoAccount(context: Context): OAuthToken {
        return suspendCancellableCoroutine { continuation ->
            UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                when {
                    error != null -> {
                        continuation.resumeWithException(error)
                    }

                    token != null -> {
                        continuation.resume(token)
                    }

                    else -> {
                        continuation.resumeWithException(RuntimeException("kakao access token 받기 실패"))
                    }
                }
            }
        }
    }

    private fun onSuccessKaKaoLogin(oAuthToken: OAuthToken, targetPage: LoginResultListener.TargetPage?) {
        UserApiClient.instance.me { user, _ ->
            if (user != null) {
                viewModel.signInWithKaKaoTokenViaServer(
                    token = oAuthToken.accessToken,
                ) { result, memberId ->
                    if (result) {
                        viewModel.setMemberData(
                            id = memberId,
                            name = user.kakaoAccount?.profile?.nickname,
                            imageUrl = user.kakaoAccount?.profile?.profileImageUrl,
                        )
                        if (targetPage == null) {
                            loginResultListener?.onLoginSuccess()
                        } else {
                            loginResultListener?.onLoginSuccess(targetPage)
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "로그인에 실패했어요.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestPermissions() {
        val hasForegroundServicePermission = PackageManager.PERMISSION_GRANTED ==
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)

        val permissionsToRequest = mutableListOf<String>()
        if (!hasForegroundServicePermission) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

    private fun handlePermissionsResult(
        permissions: Map<String, @JvmSuppressWildcards Boolean>,
    ) {
        val allGranted = permissions.values.all { it }
        if (allGranted) { // 모든 권한이 승인되었을 때
            Timber.tag("MainActivity").d("권한 승인")
        } else {
            Timber.tag("MainActivity").d("일부 권한이 거부되었습니다.")
            // 거부된 권한에 대한 처리
        }
    }

    private fun redirectToDetailOnMessageReceived() {
        val phraseId = runCatching {
            viewModel.phraseId?.toLong()
        }.getOrNull() ?: return

        navController.navigate(
            R.id.detailFragment,
            bundleOf(PHRASE_ID to phraseId)
        )
    }

    companion object {
        const val REQUEST_KEY_MOVE_TO_UPDATE = "REQUEST_KEY_MOVE_TO_UPDATE"
    }
}
