package com.silvertown.android.dailyphrase.presentation.ui.mypage.non_login

import android.os.Bundle
import android.view.View
import androidx.compose.ui.Modifier
import androidx.navigation.fragment.findNavController
import com.silvertown.android.dailyphrase.presentation.MainActivity
import com.silvertown.android.dailyphrase.presentation.databinding.FragmentNonLoginBinding
import com.silvertown.android.dailyphrase.presentation.base.BaseFragment
import com.silvertown.android.dailyphrase.presentation.util.LoginResultListener

class NonLoginFragment :
    BaseFragment<FragmentNonLoginBinding>(FragmentNonLoginBinding::inflate),
    LoginResultListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setLoginResultListener(this)

        binding.composeView.setContent {
            NonLoginScreen(
                modifier = Modifier,
                navigateToBack = {
                    findNavController().popBackStack()
                },
                onClickKaKaoLogin = {
                    (activity as MainActivity).kakaoLogin()
                }
            )
        }
    }

    override fun onLoginSuccess() {
        NonLoginFragmentDirections
            .moveToMyPageFragment()
            .also { findNavController().navigate(it) }
    }

}
