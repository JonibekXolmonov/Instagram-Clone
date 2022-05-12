package com.example.instagramclone.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.instagramclone.R
import com.example.instagramclone.adapter.HomeAdapter
import com.example.instagramclone.databinding.FragmentHomeBinding
import com.example.instagramclone.extension.viewBinding
import com.example.instagramclone.manager.AuthManager
import com.example.instagramclone.manager.DatabaseManager
import com.example.instagramclone.manager.handler.DBPostsHandler
import com.example.instagramclone.model.Post
import com.example.instagramclone.utils.BounceEdgeEffectFactory
import java.lang.Exception
import java.lang.RuntimeException


class HomeFragment : BaseFragment() {

    private val binding by viewBinding { FragmentHomeBinding.bind(it) }
    var listener: HomeListener? = null
    lateinit var adapter: HomeAdapter
    var feeds = ArrayList<Post>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView(view)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser && feeds.size > 0) {
            loadMyFeeds()
        }
    }

    /*
    onAttach is for communication of fragments
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = if (context is HomeListener) {
            context
        } else {
            throw RuntimeException("$context must implement HomeListener")
        }
    }

    /*
    onDetach is for communication of fragments
     */
    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun initView(view: View) {
        binding.apply {
            ivCamera.setOnClickListener {
                listener!!.scrollToUpload()
            }

            rvHome.edgeEffectFactory = BounceEdgeEffectFactory()

            adapter = HomeAdapter(this@HomeFragment, arrayListOf())
            adapter.onMoreClick = { imageUrl ->
                //delete here
            }
        }

        loadMyFeeds()
    }

    private fun loadMyFeeds() {
        showLoading(requireActivity())
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.loadFeeds(uid, object : DBPostsHandler {
            override fun onSuccess(posts: ArrayList<Post>) {
                dismissLoading()
                feeds.clear()
                feeds.addAll(posts)
                refreshAdapter(posts)
            }

            override fun onError(e: Exception) {
                dismissLoading()
            }
        })
    }

    private fun refreshAdapter(posts: ArrayList<Post>) {
        adapter = HomeAdapter(this, posts)
        binding.rvHome.adapter = adapter
    }

    fun likeOrUnlikePost(post: Post) {
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.likeFeedPost(uid, post)
    }


    /*
    This interface is created for communication with UploadFragment
     */
    interface HomeListener {
        fun scrollToUpload()
    }
}