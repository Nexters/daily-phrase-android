package com.silvertown.android.dailyphrase.presentation.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.silvertown.android.dailyphrase.presentation.databinding.ItemPostBinding
import com.silvertown.android.dailyphrase.presentation.ui.home.HomeFragment.Post

class PostAdapter(
    private val onPostClick: (Long) -> Unit,
) : ListAdapter<Post, PostAdapter.PostViewHolder>(diffUtil) {

    class PostViewHolder(
        private val binding: ItemPostBinding,
        onPostClick: (Long) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        init {
            binding.root.setOnClickListener { onPostClick(post.id) }
        }

        fun bind(post: Post) = with(binding) {
            this@PostViewHolder.post = post
            tvTitle.text = post.title
            tvPreviewText.text = post.previewText
            tvView.text = post.viewCount.toString()
            tvLike.text = post.likeCount.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onPostClick,
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }
        }
    }
}