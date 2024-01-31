package com.silvertown.android.dailyphrase.data.network.model.response

import com.google.gson.annotations.SerializedName

data class BasePostResponse(
    @SerializedName("hasNext")
    val hasNext: Boolean?,
    @SerializedName("page")
    val page: Int?,
    @SerializedName("phraseList")
    val postList: List<PostResponse>?,
    @SerializedName("size")
    val size: Int?,
    @SerializedName("total")
    val total: Int?,
)

