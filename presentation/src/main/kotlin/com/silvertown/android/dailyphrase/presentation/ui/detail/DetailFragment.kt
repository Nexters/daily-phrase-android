package com.silvertown.android.dailyphrase.presentation.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.compose.ui.Modifier
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.silvertown.android.dailyphrase.presentation.MainActivity
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.base.BaseFragment
import com.silvertown.android.dailyphrase.presentation.databinding.FragmentDetailBinding
import com.silvertown.android.dailyphrase.presentation.util.LoginResultListener

class DetailFragment :
    BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate),
    LoginResultListener {

    private val viewModel by viewModels<DetailViewModel>()

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
        viewModel.showLoginDialog(false)
        viewModel.updateLoginState()
        Toast.makeText(requireContext(), R.string.login_success_desc, Toast.LENGTH_SHORT).show()
    }
}
