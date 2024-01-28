package com.silvertown.android.dailyphrase.presentation.ui.mypage

import android.os.Bundle
import android.view.View
import androidx.compose.ui.Modifier
import androidx.navigation.fragment.findNavController
import com.silvertown.android.dailyphrase.presentation.databinding.FragmentMyPageBinding
import com.silvertown.android.dailyphrase.presentation.base.BaseFragment

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(FragmentMyPageBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.composeView.setContent {
            MyPageScreen(
                modifier = Modifier,
                navigateToBack = {
                    findNavController().popBackStack()
                },
                navigateToUnsubscribe = {
                    MyPageFragmentDirections
                        .moveToUnsubscribeFragment()
                        .also { findNavController().navigate(it) }
                }
            )
        }
    }
}
