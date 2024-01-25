package com.silvertown.android.dailyphrase.presentation.ui.mypage.unsubscribe

import android.os.Bundle
import android.view.View
import androidx.compose.ui.Modifier
import androidx.navigation.fragment.findNavController
import com.silvertown.android.dailyphrase.presentation.databinding.FragmentUnsubscribeBinding
import com.silvertown.android.dailyphrase.presentation.ui.base.BaseFragment

class UnsubscribeFragment :
    BaseFragment<FragmentUnsubscribeBinding>(FragmentUnsubscribeBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.composeView.setContent {
            UnsubscribeScreen(
                modifier = Modifier,
                navigationToBack = {
                    findNavController().popBackStack()
                }
            )
        }
    }
}
