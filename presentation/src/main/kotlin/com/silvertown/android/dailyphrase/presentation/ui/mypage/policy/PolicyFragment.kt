package com.silvertown.android.dailyphrase.presentation.ui.mypage.policy

import android.os.Bundle
import android.view.View
import androidx.compose.ui.Modifier
import androidx.navigation.fragment.findNavController
import com.silvertown.android.dailyphrase.presentation.base.BaseFragment
import com.silvertown.android.dailyphrase.presentation.databinding.FragmentPolicyBinding

class PolicyFragment : BaseFragment<FragmentPolicyBinding>(FragmentPolicyBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.composeView.setContent {
            PolicyScreen(
                modifier = Modifier,
                navigateToBack = {
                    findNavController().popBackStack()
                }
            )
        }
    }
}
