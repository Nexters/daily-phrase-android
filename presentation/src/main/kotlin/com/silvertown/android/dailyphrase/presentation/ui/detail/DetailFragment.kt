package com.silvertown.android.dailyphrase.presentation.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.compose.ui.Modifier
import androidx.navigation.fragment.findNavController
import com.silvertown.android.dailyphrase.presentation.MainActivity
import com.silvertown.android.dailyphrase.presentation.base.BaseFragment
import com.silvertown.android.dailyphrase.presentation.databinding.FragmentDetailBinding
import com.silvertown.android.dailyphrase.presentation.util.LoginResultListener

class DetailFragment :
    BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate),
    LoginResultListener {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setLoginResultListener(this)

        binding.composeView.setContent {
            DetailScreen(
                modifier = Modifier,
                navigateToBack = {
                    findNavController().popBackStack()
                }
            )
        }
    }

    override fun onLoginSuccess() {
        Toast.makeText(requireContext(), "로그인 성공! 다시 시도 해보세요.", Toast.LENGTH_SHORT).show()
    }
}
