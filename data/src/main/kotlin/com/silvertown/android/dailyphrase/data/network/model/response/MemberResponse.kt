package com.silvertown.android.dailyphrase.data.network.model.response

import com.google.gson.annotations.SerializedName
import com.silvertown.android.dailyphrase.domain.model.Member

data class MemberResponse(
    @SerializedName("memberId")
    val memberId: Long?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("quitAt")
    val quitAt: String?,
)

fun MemberResponse.toDomainModel() = Member(
    id = memberId ?: 0,
    name = name.orEmpty(),
    email = email.orEmpty(),
    imageUrl = imageUrl.orEmpty(),
    quitAt = quitAt.orEmpty()
)
