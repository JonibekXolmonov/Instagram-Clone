package com.example.instagramclone.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.instagramclone.R
import com.example.instagramclone.adapter.FavoriteAdapter
import com.example.instagramclone.databinding.FragmentFavoriteBinding
import com.example.instagramclone.extension.viewBinding
import com.example.instagramclone.manager.AuthManager
import com.example.instagramclone.manager.DatabaseManager
import com.example.instagramclone.manager.handler.DBPostsHandler
import com.example.instagramclone.model.Post


class FavoriteFragment : BaseFragment() {

    private val binding by viewBinding { FragmentFavoriteBinding.bind(it) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        loadLikedFeeds()
    }

    private fun loadLikedFeeds() {
        showLoading(requireActivity())
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.loadLikedFeeds(uid, object : DBPostsHandler {
            override fun onSuccess(posts: ArrayList<Post>) {
                dismissLoading()
                refreshAdapter(posts)
            }

            override fun onError(e: Exception) {
                dismissLoading()
            }
        })
    }

    private fun refreshAdapter(posts: java.util.ArrayList<Post>) {
        val adapter = FavoriteAdapter(this, posts)
        binding.rvFavorite.adapter = adapter
    }

    fun likeOrUnlikePost(post: Post) {
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.likeFeedPost(uid, post)

        loadLikedFeeds()
    }
}