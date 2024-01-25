package com.silvertown.android.dailyphrase.presentation.ui.mypage.non_login

import android.os.Bundle
import android.view.View
import androidx.compose.ui.Modifier
import androidx.navigation.fragment.findNavController
import com.silvertown.android.dailyphrase.presentation.databinding.FragmentNonLoginBinding
import com.silvertown.android.dailyphrase.presentation.ui.base.BaseFragment

class NonLoginFragment : BaseFragment<FragmentNonLoginBinding>(FragmentNonLoginBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.composeView.setContent {
            NonLoginScreen(
                modifier = Modifier,
                navigateToBack = {
                    findNavController().popBackStack()
                },
                onClickKaKaoLogin = { }
            )
        }
    }
}
