package com.silvertown.android.dailyphrase.presentation.ui.detail

import android.os.Bundle
import android.view.View
import androidx.compose.ui.Modifier
import androidx.navigation.fragment.findNavController
import com.silvertown.android.dailyphrase.presentation.base.BaseFragment
import com.silvertown.android.dailyphrase.presentation.databinding.FragmentDetailBinding

class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.composeView.setContent {
            DetailScreen(
                modifier = Modifier,
                navigateToBack = {
                    findNavController().popBackStack()
                }
            )
        }
    }
}
