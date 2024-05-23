package com.silvertown.android.dailyphrase.data.network.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.silvertown.android.dailyphrase.data.database.dao.PostDao
import com.silvertown.android.dailyphrase.data.database.dao.RemoteKeysDao
import com.silvertown.android.dailyphrase.data.database.model.PostEntity
import com.silvertown.android.dailyphrase.data.database.model.RemoteKeys
import com.silvertown.android.dailyphrase.data.network.datasource.PostDataSource
import com.silvertown.android.dailyphrase.data.network.model.response.BasePostResponse
import com.silvertown.android.dailyphrase.data.network.model.response.toEntity
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PostMediator @Inject constructor(
    private val postDao: PostDao,
    private val remoteKeysDao: RemoteKeysDao,
    private val postDataSource: PostDataSource,
) : RemoteMediator<Int, PostEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>,
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    remoteKeys?.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    remoteKeys?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
            }

            val postsResult = postDataSource.getPosts(
                page = page,
                size = state.config.pageSize,
            )

            postsResult.result?.let { savePosts(it, loadType, page) }

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
        page: Int,
    ) {
        response.let {
            val entities = it.postList?.map { item ->
                item.toEntity()
            }

            val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
            val nextKey = if (isEndOfPagination(response.hasNext)) null else page + 1
            entities
                ?.map { post ->
                    RemoteKeys(
                        id = post.phraseId,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }?.let { remoteKeys ->
                    remoteKeysDao.insertAll(remoteKeys)
                }

            postDao.savePostsAndDeleteIfRequired(
                posts = entities.orEmpty(),
                shouldDelete = loadType == LoadType.REFRESH,
            )
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PostEntity>): RemoteKeys? {
        return state
            .lastItemOrNull()
            ?.let { post ->
                remoteKeysDao.getRemoteKeys(post.phraseId)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, PostEntity>): RemoteKeys? {
        return state
            .firstItemOrNull()
            ?.let { post ->
                remoteKeysDao.getRemoteKeys(post.phraseId)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, PostEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)
                ?.phraseId
                ?.let { phraseId ->
                    remoteKeysDao.getRemoteKeys(phraseId)
                }
        }
    }

    private fun isEndOfPagination(hasNext: Boolean?): Boolean {
        return !(hasNext ?: false)
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }
}
