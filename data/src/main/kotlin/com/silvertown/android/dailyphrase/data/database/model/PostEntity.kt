package com.silvertown.android.dailyphrase.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.silvertown.android.dailyphrase.domain.model.Post

@Entity(
    tableName = "post_source"
)
data class PostEntity(
    @PrimaryKey
    val phraseId: Long,
    val title: String,
    val content: String,
    val imageUrl: String,
    val imageRatio: String,
    val viewCount: Int,
    val likeCount: Int,
    val isLike: Boolean,
    val isFavorite: Boolean,
)

fun PostEntity.toDomainModel(): Post {
    return Post(
        phraseId = phraseId,
        title = title,
        content = content,
        imageUrl = imageUrl,
        imageRatio = imageRatio,
        viewCount = viewCount,
        likeCount = likeCount,
        isLike = isLike,
        isFavorite = isFavorite
    )
}
