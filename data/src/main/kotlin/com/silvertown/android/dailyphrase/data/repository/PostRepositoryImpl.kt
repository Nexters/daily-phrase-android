package com.silvertown.android.dailyphrase.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.silvertown.android.dailyphrase.data.database.dao.PostDao
import com.silvertown.android.dailyphrase.data.database.dao.RemoteKeysDao
import com.silvertown.android.dailyphrase.data.database.model.toDomainModel
import com.silvertown.android.dailyphrase.data.datastore.datasource.MemberPreferencesDataSource
import com.silvertown.android.dailyphrase.data.network.common.toResultModel
import com.silvertown.android.dailyphrase.data.network.datasource.PostDataSource
import com.silvertown.android.dailyphrase.data.network.mediator.PostMediator
import com.silvertown.android.dailyphrase.data.network.model.request.FavoritesRequest
import com.silvertown.android.dailyphrase.data.network.model.request.LikeRequest
import com.silvertown.android.dailyphrase.data.network.model.response.toDomainModel
import com.silvertown.android.dailyphrase.domain.model.Bookmark
import com.silvertown.android.dailyphrase.domain.model.Favorites
import com.silvertown.android.dailyphrase.domain.model.Like
import com.silvertown.android.dailyphrase.domain.model.Post
import com.silvertown.android.dailyphrase.domain.model.Result
import com.silvertown.android.dailyphrase.domain.model.mapResultModel
import com.silvertown.android.dailyphrase.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postDao: PostDao,
    private val remoteKeysDao: RemoteKeysDao,
    private val postDataSource: PostDataSource,
    private val memberPreferencesDataSource: MemberPreferencesDataSource,
) : PostRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPosts(): Flow<PagingData<Post>> {
        val pager = Pager(
            config = PagingConfig(
                pageSize = PAGING_PAGE_SIZE,
                prefetchDistance = PAGING_PREFETCH_DISTANCE,
                initialLoadSize = PAGING_INITIAL_LOAD_SIZE,
            ),
            remoteMediator = PostMediator(
                postDao = postDao,
                remoteKeysDao = remoteKeysDao,
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

    override suspend fun getPost(phraseId: Long): Result<Post> =
        postDataSource
            .getPost(phraseId)
            .toResultModel { it.result?.toDomainModel() }

    override suspend fun saveLike(phraseId: Long): Result<Like> =
        postDataSource
            .saveLike(
                LikeRequest(
                    memberId = memberPreferencesDataSource.getMemberId(),
                    phraseId = phraseId
                )
            ).toResultModel { it.result?.toDomainModel() }

    override suspend fun deleteLike(phraseId: Long): Result<Like> =
        postDataSource
            .deleteLike(
                memberId = memberPreferencesDataSource.getMemberId(),
                phraseId = phraseId
            ).toResultModel { it.result?.toDomainModel() }

    override suspend fun getFavorites(): Flow<Result<Bookmark>> {
        return postDataSource
            .getFavorites(memberId = memberPreferencesDataSource.getMemberId())
            .mapResultModel { it.result?.toDomainModel() }
    }

    override suspend fun saveFavorites(phraseId: Long): Result<Favorites> =
        postDataSource
            .saveFavorites(
                FavoritesRequest(
                    memberId = memberPreferencesDataSource.getMemberId(),
                    phraseId = phraseId
                )
            )
            .toResultModel { it.result?.toDomainModel() }

    override suspend fun deleteFavorites(phraseId: Long): Result<Favorites> =
        postDataSource
            .deleteFavorites(
                memberId = memberPreferencesDataSource.getMemberId(),
                phraseId = phraseId
            )
            .toResultModel { it.result?.toDomainModel() }

    override suspend fun updateLikeState(
        phraseId: Long,
        isLike: Boolean,
        count: Int,
    ) {
        postDao.updateLikeState(phraseId, isLike, count)
    }

    override suspend fun updateFavoriteState(phraseId: Long, isFavorite: Boolean) {
        postDao.updateFavoriteState(phraseId, isFavorite)
    }

    override suspend fun updateCounts(phraseId: Long, likeCount: Int, viewCount: Int) {
        postDao.updateCounts(phraseId, likeCount, viewCount)
    }

    companion object {
        private const val PAGING_PAGE_SIZE = 6
        private const val PAGING_PREFETCH_DISTANCE = 6
        private const val PAGING_INITIAL_LOAD_SIZE = 12
    }
}
