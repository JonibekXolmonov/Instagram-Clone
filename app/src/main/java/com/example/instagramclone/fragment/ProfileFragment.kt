package com.example.instagramclone.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.instagramclone.R
import com.example.instagramclone.SignInActivity
import com.example.instagramclone.adapter.ProfileAdapter
import com.example.instagramclone.databinding.FragmentHomeBinding
import com.example.instagramclone.databinding.FragmentProfileBinding
import com.example.instagramclone.databinding.FragmentUploadBinding
import com.example.instagramclone.extension.viewBinding
import com.example.instagramclone.manager.AuthManager
import com.example.instagramclone.manager.DatabaseManager
import com.example.instagramclone.manager.StorageManager
import com.example.instagramclone.manager.handler.DBPostsHandler
import com.example.instagramclone.manager.handler.DBUserHandler
import com.example.instagramclone.manager.handler.StorageHandler
import com.example.instagramclone.model.Post
import com.example.instagramclone.model.User
import com.example.instagramclone.utils.BounceEdgeEffectFactory
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import java.lang.Exception


class ProfileFragment : BaseFragment() {

    private val binding by viewBinding { FragmentProfileBinding.bind(it) }
    var pickedPhoto: Uri? = null
    var allPhotos = ArrayList<Uri>()
    val TAG = ProfileFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        binding.apply {
            ivLogout.setOnClickListener {
                AuthManager.signOut()
                callSignInActivity()
            }

            ivProfile.setOnClickListener {
                pickFishBunPhoto()
            }

            rvProfile.edgeEffectFactory = BounceEdgeEffectFactory()
        }

        loadUserInfo()
        loadMyPosts()
    }

    private fun loadMyPosts() {
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.loadPosts(uid, object : DBPostsHandler {
            override fun onSuccess(posts: ArrayList<Post>) {
                binding.tvPostNumber.text = posts.size.toString()
                d(TAG, "onSuccess: $posts")
                refreshAdapter(posts)
            }

            override fun onError(e: Exception) {

            }
        })
    }

    private fun callSignInActivity() {
        startActivity(Intent(requireContext(), SignInActivity::class.java))
    }

    private fun loadUserInfo() {
        DatabaseManager.loadUser(AuthManager.currentUser()!!.uid, object : DBUserHandler {
            override fun onSuccess(user: User?) {
                if (user != null) {
                    showUserInfo(user)
                }
            }

            override fun onError(e: Exception) {}
        })
    }

    private fun showUserInfo(user: User) {
        binding.apply {
            tvFullname.text = user.fullname
            tvEmail.text = user.email
            Glide.with(requireContext())
                .load(user.userImg)
                .placeholder(R.drawable.img_default_user)
                .error(R.drawable.img_default_user)
                .into(ivProfile)
        }
    }

    private fun refreshAdapter(posts: java.util.ArrayList<Post>) {
        binding.rvProfile.adapter = ProfileAdapter(this, posts)
    }

    private fun pickFishBunPhoto() {
        FishBun.with(this)
            .setImageAdapter(GlideAdapter())
            .setMaxCount(1)
            .setMinCount(1)
            .setSelectedImages(allPhotos)
            .startAlbumWithActivityResultCallback(photoLauncher)
    }

    private val photoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            allPhotos = it.data?.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf()
            pickedPhoto = allPhotos[0]
            uploadUserPhoto()
        }

    private fun uploadUserPhoto() {
        if (pickedPhoto == null) return
        StorageManager.uploadUserPhoto(pickedPhoto!!, object : StorageHandler {
            override fun onSuccess(imgUrl: String) {
                DatabaseManager.updateUserImg(imgUrl)
                binding.ivProfile.setImageURI(pickedPhoto)
            }

            override fun onError(exception: Exception?) {}
        })
    }

    private fun loadPosts(): ArrayList<Post> {
        val items = ArrayList<Post>()

        return items
    }
}