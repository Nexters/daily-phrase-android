package com.silvertown.android.dailyphrase.presentation.ui.bookmark

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.silvertown.android.dailyphrase.presentation.databinding.FragmentBookmarkBinding
import com.silvertown.android.dailyphrase.presentation.ui.base.BaseFragment

class BookmarkFragment : BaseFragment<FragmentBookmarkBinding>(FragmentBookmarkBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        binding.tvAllPhrase.setOnClickListener {
            findNavController().popBackStack() // TODO: 수정
        }
    }
}
