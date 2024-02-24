package com.silvertown.android.dailyphrase.presentation.ui.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.silvertown.android.dailyphrase.domain.model.Post
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.databinding.ItemPostBinding
import com.silvertown.android.dailyphrase.presentation.extensions.formatNumberWithComma

class BookmarkAdapter(
    private val onPostClick: (Long) -> Unit,
    private val onClickBookmark: (Long) -> Unit,
    private val onClickLike: (Long, Boolean) -> Unit,
) : ListAdapter<Post, BookmarkAdapter.BookmarkViewHolder>(diffUtil) {

    class BookmarkViewHolder(
        private val binding: ItemPostBinding,
        onPostClick: (Long) -> Unit,
        onClickBookmark: (Long) -> Unit,
        onClickLike: (Long, Boolean) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        init {
            binding.root.setOnClickListener { onPostClick(post.phraseId) }
            binding.clLike.setOnClickListener {
                onClickLike(
                    post.phraseId,
                    post.isLike,
                )
            }
            binding.clBookmark.setOnClickListener {
                onClickBookmark(post.phraseId)
            }
            // View 아이콘 클릭 시 root영역에 대한 클릭리스너와 중복을 피하기 위한 임시 리스너 활성
            binding.clView.setOnClickListener { }
        }

        fun bind(post: Post) = with(binding) {
            this@BookmarkViewHolder.post = post
            tvTitle.text = post.title.replace("\n", " ")
            tvContent.text = post.content.replace("\n", " ")
            tvView.text = post.viewCount.formatNumberWithComma()
            tvLike.text = post.likeCount.formatNumberWithComma()
            tvBookmark.setTextColor(
                if (post.isFavorite) {
                    ContextCompat.getColor(itemView.context, R.color.black)
                } else {
                    ContextCompat.getColor(itemView.context, R.color.gray)
                }
            )
            tvBookmark.typeface = if (post.isFavorite) {
                ResourcesCompat.getFont(itemView.context, R.font.pretendard_medium)
            } else {
                ResourcesCompat.getFont(itemView.context, R.font.pretendard_regular)
            }
            ivImage.isGone = post.imageUrl.isEmpty()

            val bookmarkRes =
                if (post.isFavorite) R.drawable.ic_bookmark_fill_60 else R.drawable.ic_bookmark_24
            ivBookmark.setImageResource(bookmarkRes)

            val likeRes = if (post.isLike) R.drawable.ic_like_fill_60 else R.drawable.ic_like_60
            ivLike.setImageResource(likeRes)

            Glide.with(itemView)
                .load(post.imageUrl)
                .transform(CenterCrop(), RoundedCorners(16))
                .into(ivImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        return BookmarkViewHolder(
            ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onPostClick,
            onClickBookmark,
            onClickLike,
        )
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.phraseId == newItem.phraseId
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }
        }
    }
}
