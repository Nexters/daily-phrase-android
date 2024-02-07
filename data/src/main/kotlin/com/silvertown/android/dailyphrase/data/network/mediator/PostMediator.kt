package com.silvertown.android.dailyphrase.data.network.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.silvertown.android.dailyphrase.data.database.dao.PostDao
import com.silvertown.android.dailyphrase.data.database.model.PostEntity
import com.silvertown.android.dailyphrase.data.network.common.onException
import com.silvertown.android.dailyphrase.data.network.common.onSuccess
import com.silvertown.android.dailyphrase.data.network.datasource.PostDataSource
import com.silvertown.android.dailyphrase.data.network.model.response.BasePostResponse
import com.silvertown.android.dailyphrase.data.network.model.response.toEntity
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PostMediator @Inject constructor(
    private val postDao: PostDao,
    private val postDataSource: PostDataSource,
) : RemoteMediator<Int, PostEntity>() {
    private var loadKey = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>,
    ): MediatorResult {
        lateinit var result: MediatorResult

        return try {
            when (loadType) {
                LoadType.REFRESH -> {
                    loadKey = 1
                }

                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )

                LoadType.APPEND -> {
                    loadKey++
                }
            }

            val postsResult = postDataSource.getPosts(
                page = loadKey,
                size = state.config.pageSize
            )

            postsResult.onSuccess { response ->
                val basePostResponse = response.result
                basePostResponse?.let { savePosts(it, loadType) }

                result = MediatorResult.Success(
                    endOfPaginationReached = isEndOfPagination(basePostResponse)
                )
            }.onException { e ->
                result = MediatorResult.Error(e)
            }

            return result
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun savePosts(
        response: BasePostResponse,
        loadType: LoadType,
    ) {
        response.let {
            val entities = it.postList?.map { item ->
                item.toEntity()
            }

            postDao.savePostsAndDeleteIfRequired(
                posts = entities.orEmpty(),
                shouldDelete = loadType == LoadType.REFRESH
            )
        }
    }

    private fun isEndOfPagination(result: BasePostResponse?): Boolean {
        return !(result?.hasNext ?: false)
    }
}
