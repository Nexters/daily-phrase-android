package com.silvertown.android.dailyphrase.data.network.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.silvertown.android.dailyphrase.data.database.dao.PostDao
import com.silvertown.android.dailyphrase.data.database.model.PostEntity
import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
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
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>,
    ): MediatorResult {
        var result: MediatorResult? = null

        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 0

                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )

                LoadType.APPEND -> {
                    state.lastItemOrNull()?.let { lastItem ->
                        (lastItem.phraseId.toInt() / state.config.pageSize) + 1
                    } ?: 1
                }
            }

            val postsResult = postDataSource.getPosts(
                page = loadKey,
                size = state.config.pageSize
            )

            postsResult.onSuccess { response ->
                savePosts(response, loadType)

                result =
                    isEndOfPagination(postsResult).let {
                        MediatorResult.Success(
                            endOfPaginationReached = it
                        )
                    }
            }.onException { e ->
                result = MediatorResult.Error(e)
            }

            return result
                ?: throw IllegalStateException("Unexpected scenario in PostMediator.load")
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
            val entities = it.postList.map { item ->
                item.toEntity()
            }

            postDao.savePostsAndDeleteIfRequired(
                posts = entities,
                shouldDelete = loadType == LoadType.REFRESH
            )
        }
    }

    private fun isEndOfPagination(result: ApiResponse<BasePostResponse>) =
        if (result is ApiResponse.Success) {
            result.result.hasNext
        } else {
            true
        }
}
