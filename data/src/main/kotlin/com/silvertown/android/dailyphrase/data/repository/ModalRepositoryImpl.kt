package com.silvertown.android.dailyphrase.data.repository

import com.silvertown.android.dailyphrase.data.network.common.toResultModel
import com.silvertown.android.dailyphrase.data.network.datasource.ModalDataSource
import com.silvertown.android.dailyphrase.domain.model.Modal
import com.silvertown.android.dailyphrase.domain.model.Result
import com.silvertown.android.dailyphrase.domain.repository.ModalRepository
import javax.inject.Inject

class ModalRepositoryImpl @Inject constructor(
    private val modalDataSource: ModalDataSource,
) : ModalRepository {
    override suspend fun getModals(): Result<List<Modal>> {
        return modalDataSource.getModals().toResultModel { it.result?.map { it.toDomainModel() } }
    }
}
