package com.silvertown.android.dailyphrase.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.silvertown.android.dailyphrase.domain.model.Post

@Entity(
    tableName = "post_source"
)
data class PostEntity(
    @PrimaryKey
    val id: Long,
    val title: String,
    val previewText: String,
    val imageUrl: String?,
    val viewCount: Long,
    val likeCount: Long,
    val isBookmarked: Boolean,
)

fun PostEntity.toDomainModel(): Post {
    return Post(
        id = id,
        title = title,
        previewText = previewText,
        imageUrl = imageUrl,
        viewCount = viewCount,
        likeCount = likeCount,
        isBookmarked = isBookmarked
    )
}
