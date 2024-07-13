package com.silvertown.android.dailyphrase.presentation.util

import com.silvertown.android.dailyphrase.presentation.R

enum class ActionType(val messageRes: Int) {
    LIKE(R.string.login_and_like_message),
    BOOKMARK(R.string.login_and_bookmark_message),
    SHARE(R.string.login_and_share_message),
    NONE(R.string.login_and_share_message);
}
