package com.silvertown.android.dailyphrase.data.network.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.silvertown.android.dailyphrase.data.database.dao.PostDao
import com.silvertown.android.dailyphrase.data.database.model.PostEntity
import com.silvertown.android.dailyphrase.data.network.datasource.PostDataSource
import com.silvertown.android.dailyphrase.data.network.model.response.BasePostResponse
import com.silvertown.android.dailyphrase.data.network.model.response.toEntity
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val STARTING_PAGE_INDEX = 1

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
        return try {
            when (loadType) {
                LoadType.REFRESH -> {
                    loadKey = STARTING_PAGE_INDEX
                }

                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true,
                )

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        loadKey = STARTING_PAGE_INDEX
                    } else {
                        loadKey++
                    }
                }
            }

            val postsResult = postDataSource.getPosts(
                page = loadKey,
                size = state.config.pageSize,
            )

            postsResult.result?.let { savePosts(it, loadType) }

            MediatorResult.Success(
                endOfPaginationReached = isEndOfPagination(postsResult.result?.hasNext),
            )
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
                shouldDelete = loadType == LoadType.REFRESH,
            )
        }
    }

    private fun isEndOfPagination(hasNext: Boolean?): Boolean {
        return !(hasNext ?: false)
    }
}
