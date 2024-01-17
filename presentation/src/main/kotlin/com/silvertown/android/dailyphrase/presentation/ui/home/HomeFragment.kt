package com.silvertown.android.dailyphrase.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.silvertown.android.dailyphrase.presentation.databinding.FragmentHomeBinding
import com.silvertown.android.dailyphrase.presentation.ui.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private lateinit var adapter: PostAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        adapter = PostAdapter { moveToPost() }
        binding.rvPost.adapter = adapter
        adapter.submitList(getData())
    }

    private fun getData(): List<Post> {
        return listOf(
            Post(
                id = 1,
                title = "타이틀",
                previewText = "짜잔",
                imageUrl = "임시 데이터입니다.",
                viewCount = 250,
                likeCount = 200,
                isBookmarked = true,
            ),
            Post(
                id = 2,
                title = "타이틀2",
                previewText = "짜잔2",
                imageUrl = "임시 데이터입니다.2",
                viewCount = 130,
                likeCount = 90,
                isBookmarked = true,
            ),
        )
    }

    private fun moveToPost() {
        HomeFragmentDirections
            .moveToPostFragment()
            .also { findNavController().navigate(it) }
    }

    // TODO JH: Domain 모듈에 파일 생성이 잘 안돼서 임시로 여기에 만듬
    data class Post(
        val id: Long,
        val title: String,
        val previewText: String,
        val imageUrl: String,
        val viewCount: Long,
        val likeCount: Long,
        val isBookmarked: Boolean,
    )
}
