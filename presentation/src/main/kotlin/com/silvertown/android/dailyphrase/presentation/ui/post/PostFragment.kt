package com.silvertown.android.dailyphrase.presentation.ui.post

import android.os.Bundle
import android.view.View
import androidx.compose.ui.Modifier
import com.silvertown.android.dailyphrase.presentation.databinding.FragmentPostBinding
import com.silvertown.android.dailyphrase.presentation.ui.base.BaseFragment

class PostFragment : BaseFragment<FragmentPostBinding>(FragmentPostBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.composeView.setContent {
            PostScreen(
                modifier = Modifier
            )
        }
    }
}
