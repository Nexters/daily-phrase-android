package com.silvertown.android.dailyphrase.presentation.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.silvertown.android.dailyphrase.domain.model.Post
import com.silvertown.android.dailyphrase.presentation.databinding.ItemPostBinding

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
            binding.ivImage.isGone = post.imageUrl.isNullOrEmpty()

            Glide.with(itemView)
                .load(post.imageUrl)
                .transform(CenterCrop(), RoundedCorners(16))
                .into(binding.ivImage)
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
