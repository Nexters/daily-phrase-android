package com.silvertown.android.dailyphrase.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.silvertown.android.dailyphrase.data.database.dao.PostDao
import com.silvertown.android.dailyphrase.data.database.model.toDomainModel
import com.silvertown.android.dailyphrase.data.network.datasource.PostDataSource
import com.silvertown.android.dailyphrase.data.network.mediator.PostMediator
import com.silvertown.android.dailyphrase.domain.model.Post
import com.silvertown.android.dailyphrase.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postDao: PostDao,
    private val postDataSource: PostDataSource,
) : PostRepository {
    companion object {
        private const val PAGING__PAGE_SIZE = 10
        private const val PAGING__PREFETCH_DISTANCE = 5
        private const val PAGING__INITIAL_LOAD_SIZE = 10
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPosts(): Flow<PagingData<Post>> {
        val pager = Pager(
            config = PagingConfig(
                pageSize = PAGING__PAGE_SIZE,
                prefetchDistance = PAGING__PREFETCH_DISTANCE,
                initialLoadSize = PAGING__INITIAL_LOAD_SIZE
            ),
            remoteMediator = PostMediator(
                postDao = postDao,
                postDataSource = postDataSource,
            ),
            pagingSourceFactory = {
                postDao.pagingSource()
            }
        )

        return pager.flow.map { pagingData ->
            pagingData.map { it.toDomainModel() }
        }
    }
}
