package com.silvertown.android.dailyphrase.presentation.ui.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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
                    post.isLike
                )
            }
            binding.clBookmark.setOnClickListener {
                onClickBookmark(post.phraseId)
            }
        }

        fun bind(post: Post) = with(binding) {
            this@BookmarkViewHolder.post = post
            tvTitle.text = post.title
            tvContent.text = post.content
            tvView.text = post.viewCount.formatNumberWithComma()
            tvLike.text = post.likeCount.formatNumberWithComma()


            val bookmarkRes =
                if (post.isFavorite) R.drawable.ic_bookmark_fill_60 else R.drawable.ic_bookmark_24
            binding.ivBookmark.setImageResource(bookmarkRes)

            val likeRes = if (post.isLike) R.drawable.ic_like_fill_60 else R.drawable.ic_like_60
            binding.ivLike.setImageResource(likeRes)
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
