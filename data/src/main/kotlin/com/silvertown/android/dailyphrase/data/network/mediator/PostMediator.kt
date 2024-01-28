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
import com.silvertown.android.dailyphrase.data.network.model.response.PostsResponse
import com.silvertown.android.dailyphrase.data.network.model.response.toEntity
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * TODO: 임시 처리 - API 명세에 나오면 수정 예정
 */
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
                LoadType.REFRESH -> 1

                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )

                LoadType.APPEND -> {
                    state.lastItemOrNull()?.let { lastItem ->
                        (lastItem.id.toInt() / state.config.pageSize) + 1
                    } ?: 1
                }
            }

            val postsResult = postDataSource.getPosts(
                page = loadKey,
                limit = state.config.pageSize
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
        postsResponse: PostsResponse,
        loadType: LoadType,
    ) {
        postsResponse.let {
            val entities = it.posts.map { item ->
                item.toEntity()
            }

            postDao.savePostsAndDeleteIfRequired(
                posts = entities,
                shouldDelete = loadType == LoadType.REFRESH
            )
        }
    }

    private fun isEndOfPagination(postsResult: ApiResponse<PostsResponse>) =
        if (postsResult is ApiResponse.Success) {
            postsResult.data.posts.isEmpty()
        } else {
            true
        }
}
