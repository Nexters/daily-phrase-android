package com.silvertown.android.dailyphrase.presentation.ui.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.silvertown.android.dailyphrase.domain.model.Post
import com.silvertown.android.dailyphrase.presentation.databinding.ItemPostBinding

class BookmarkAdapter(
    private val onClickBookmark: (Long) -> Unit,
) : ListAdapter<Post, BookmarkAdapter.BookmarkViewHolder>(diffUtil) {

    class BookmarkViewHolder(
        private val binding: ItemPostBinding,
        onBookmarkClick: (Long) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        init {
            binding.root.setOnClickListener { onBookmarkClick(post.phraseId) }
        }

        fun bind(post: Post) = with(binding) {
            this@BookmarkViewHolder.post = post
            tvTitle.text = post.title
            tvContent.text = post.content
            tvView.text = post.viewCount.toString()
            tvLike.text = post.likeCount.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        return BookmarkViewHolder(
            ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClickBookmark,
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