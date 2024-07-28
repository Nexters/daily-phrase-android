package com.silvertown.android.dailyphrase.domain.repository

import com.silvertown.android.dailyphrase.domain.model.Modal
import com.silvertown.android.dailyphrase.domain.model.Result

interface ModalRepository {
    suspend fun getModals(): Result<List<Modal>>
}