package com.silvertown.android.dailyphrase.data.network.model.response

import com.google.gson.annotations.SerializedName

data class PostsResponse(
    @SerializedName("list")
    val posts: List<PostResponse>,
)
