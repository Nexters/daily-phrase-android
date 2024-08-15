package com.silvertown.android.dailyphrase.data.network.model.response

import com.google.gson.annotations.SerializedName

data class ShouldShowGetTicketPopup(
    @SerializedName("memberId")
    val memberId: Int,
    @SerializedName("showGetTicketPopup")
    val showGetTicketPopup: Boolean,
)
