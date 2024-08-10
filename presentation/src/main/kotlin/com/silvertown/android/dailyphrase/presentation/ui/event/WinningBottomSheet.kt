package com.silvertown.android.dailyphrase.presentation.ui.event

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.silvertown.android.dailyphrase.presentation.base.BaseDialogFragment
import com.silvertown.android.dailyphrase.presentation.databinding.BottomSheetWinningBinding

class WinningBottomSheet : BaseDialogFragment<BottomSheetWinningBinding>(BottomSheetWinningBinding::inflate) {
    private val dimAmount = 0.45f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etPhoneNumber.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        binding.etPhoneNumber.addTextChangedListener {
            binding.tvInputPhoneNumber.isEnabled = (it.toString().length == 13)
        }
        binding.ivClose.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.tvInputPhoneNumber.setOnClickListener {
            findNavController().popBackStack()
            setFragmentResult(
                REQUEST_KEY_ENTERED_PHONE_NUMBER,
                bundleOf(BUNDLE_KEY_PHONE_NUMBER to binding.etPhoneNumber.text.toString()),
            )
        }
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.apply {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
            setGravity(Gravity.BOTTOM)
            setDimAmount(dimAmount)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onResume() {
        super.onResume()

        binding.etPhoneNumber.postDelayed({
            binding.etPhoneNumber.requestFocus()
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.etPhoneNumber, InputMethodManager.SHOW_IMPLICIT)
        }, 300)
    }

    companion object {
        const val REQUEST_KEY_ENTERED_PHONE_NUMBER = "REQUEST_KEY_ENTERED_PHONE_NUMBER"
        const val BUNDLE_KEY_PHONE_NUMBER = "BUNDLE_KEY_PHONE_NUMBER"
    }
}
