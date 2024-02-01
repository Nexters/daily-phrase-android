package com.silvertown.android.dailyphrase.data.network.model.response

import com.google.gson.annotations.SerializedName
import com.silvertown.android.dailyphrase.domain.model.Bookmark

data class BookmarkResponse(
    @SerializedName("total")
    val total: Long?,
    @SerializedName("phraseList")
    val bookmarkList: List<PostResponse>?,
)

fun BookmarkResponse.toDomainModel() = Bookmark(
    total = total ?: 0,
    bookmarkList = bookmarkList?.map { it.toDomainModel() } ?: emptyList()
)
