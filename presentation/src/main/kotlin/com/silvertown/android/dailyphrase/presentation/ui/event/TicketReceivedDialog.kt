package com.silvertown.android.dailyphrase.presentation.ui.event

import android.app.ActionBar
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.base.BaseDialogFragment
import com.silvertown.android.dailyphrase.presentation.databinding.DialogTicketReceivedBinding

class TicketReceivedDialog : BaseDialogFragment<DialogTicketReceivedBinding>(DialogTicketReceivedBinding::inflate) {
    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT,
        )
        dialog?.window?.setBackgroundDrawableResource(R.drawable.bg_rectangle_white_r_8)
    }
}