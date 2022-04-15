package com.example.instagramclone.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.instagramclone.R
import com.example.instagramclone.adapter.HomeAdapter
import com.example.instagramclone.databinding.FragmentHomeBinding
import com.example.instagramclone.extension.viewBinding
import com.example.instagramclone.model.Post
import com.example.instagramclone.utils.BounceEdgeEffectFactory
import java.lang.RuntimeException


class HomeFragment : BaseFragment() {

    private val binding by viewBinding { FragmentHomeBinding.bind(it) }
    var listener: HomeListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
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

    private fun initView() {
        binding.apply {
            ivCamera.setOnClickListener {
                listener!!.scrollToUpload()
            }

            rvHome.edgeEffectFactory = BounceEdgeEffectFactory()
        }

        refreshAdapter(loadPosts())
    }

    private fun refreshAdapter(posts: java.util.ArrayList<Post>) {
        binding.rvHome.adapter = HomeAdapter(posts)
    }

    private fun loadPosts(): ArrayList<Post> {
        return ArrayList<Post>().apply {
            this.add(Post("https://images.unsplash.com/photo-1433086966358-54859d0ed716?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80"))
            this.add(Post("https://images.unsplash.com/photo-1648737966968-5f50e6bf9e46?ixlib=rb-1.2.1&ixid=MnwxMjA3fDF8MHxlZGl0b3JpYWwtZmVlZHwyMXx8fGVufDB8fHx8&auto=format&fit=crop&w=400&q=60"))
            this.add(Post("https://images.unsplash.com/photo-1593508512255-86ab42a8e620?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=578&q=80"))
            this.add(Post("https://images.unsplash.com/photo-1649585067848-50efdd21835b?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwxOHx8fGVufDB8fHx8&auto=format&fit=crop&w=400&q=60"))
        }
    }

    /*
    This interface is created for communication with UploadFragment
     */
    interface HomeListener {
        fun scrollToUpload()
    }
}