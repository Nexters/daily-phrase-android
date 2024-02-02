package com.silvertown.android.dailyphrase.domain.model

data class Member(
    val id: Long = DEFAULT_ID.toLong(),
    val name: String = DEFAULT_NAME,
    val imageUrl: String = DEFAULT_IMAGE_URL,
    val email: String = DEFAULT_EMAIL,
    val quitAt: String = DEFAULT_QUIT_AT,
) {
    companion object {
        const val DEFAULT_ID = 0
        const val DEFAULT_NAME = "User"
        const val DEFAULT_IMAGE_URL =
            "https://cdn.pixabay.com/photo/2015/06/25/04/50/hand-print-820913_1280.jpg"
        const val DEFAULT_EMAIL = ""
        const val DEFAULT_QUIT_AT = ""
    }
}
