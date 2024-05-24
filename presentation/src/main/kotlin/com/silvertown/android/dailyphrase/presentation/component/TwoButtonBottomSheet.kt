package com.silvertown.android.dailyphrase.presentation.component

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.base.BaseBottomSheet
import com.silvertown.android.dailyphrase.presentation.databinding.DialogFragmentTwoButtonBinding
import kotlinx.parcelize.Parcelize


class TwoButtonBottomSheet :
    BaseBottomSheet<DialogFragmentTwoButtonBinding>(DialogFragmentTwoButtonBinding::inflate) {

    private val args: TwoButtonBottomSheetArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(args.twoButtonBottomSheetArg) {
            Glide.with(requireContext())
                .load(imageUrl)
                .into(binding.ivModal)

            binding.btnLeft.text = leftButtonMessage ?: getString(R.string.close)
            binding.btnRight.text = rightButtonMessage ?: getString(R.string.confirm)
        }

        initListeners()
    }

    private fun initListeners() {
        binding.btnLeft.setOnClickListener {
            dismiss()
        }
        binding.btnRight.setOnClickListener {
            dismiss()
            setFragmentResult(args.twoButtonBottomSheetArg.requestKey, bundleOf())
        }
    }

    @Parcelize
    data class TwoButtonBottomSheetArg(
        val imageUrl: String,
        val leftButtonMessage: String?,
        val rightButtonMessage: String?,
        val requestKey: String,
    ) : Parcelable
}
