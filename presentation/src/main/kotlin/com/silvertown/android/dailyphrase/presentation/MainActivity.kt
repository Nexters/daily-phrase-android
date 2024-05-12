package com.silvertown.android.dailyphrase.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.silvertown.android.dailyphrase.presentation.component.LoadingDialog
import com.silvertown.android.dailyphrase.presentation.databinding.ActivityMainBinding
import com.silvertown.android.dailyphrase.presentation.util.LoginResultListener
import dagger.hilt.android.AndroidEntryPoint
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

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController =
            (supportFragmentManager.findFragmentById(R.id.fcv_nav_host) as NavHostFragment).navController

        loadingDialog = LoadingDialog(this)

        initObserve()
    }

    fun setLoginResultListener(
        listener: LoginResultListener,
    ) {
        this.loginResultListener = listener
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        navController.handleDeepLink(intent)
    }

    private fun initObserve() {
        // TODO : API 완성된 이후 나머지 작업 예정. 임시로 토스트를 띄워둠
        lifecycleScope.launch {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    MainViewModel.UiEvent.ForceUpdate -> {
                        Toast.makeText(this@MainActivity, "강제 업데이트", Toast.LENGTH_SHORT).show()
                    }

                    MainViewModel.UiEvent.NeedUpdate -> {
                        Toast.makeText(this@MainActivity, "업데이트 필요", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun kakaoLogin() {
        lifecycleScope.launch {
            kotlin.runCatching {
                loadingDialog.show()
                loginWithKakaoOrThrow(this@MainActivity)
            }.onSuccess { oAuthToken ->
                onSuccessKaKaoLogin(oAuthToken)
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

    private fun onSuccessKaKaoLogin(oAuthToken: OAuthToken) {
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
                        loginResultListener?.onLoginSuccess()
                    } else {
                        Toast.makeText(this@MainActivity, "로그인에 실패했어요.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
}
