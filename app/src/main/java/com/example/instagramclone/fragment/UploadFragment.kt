package com.example.instagramclone.fragment

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import com.example.instagramclone.R
import com.example.instagramclone.databinding.FragmentUploadBinding
import com.example.instagramclone.extension.viewBinding
import com.example.instagramclone.manager.AuthManager
import com.example.instagramclone.manager.DatabaseManager
import com.example.instagramclone.manager.StorageManager
import com.example.instagramclone.manager.handler.DBPostHandler
import com.example.instagramclone.manager.handler.DBUserHandler
import com.example.instagramclone.manager.handler.DBUsersHandler
import com.example.instagramclone.manager.handler.StorageHandler
import com.example.instagramclone.model.Post
import com.example.instagramclone.model.User
import com.example.instagramclone.utils.Logger
import com.example.instagramclone.utils.Utils
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import java.lang.Exception
import java.lang.RuntimeException

/*
In UploadFragment user can upload a post with photo and caption
 */
class UploadFragment : BaseFragment() {

    val TAG = UploadFragment::class.java.simpleName
    private val binding by viewBinding { FragmentUploadBinding.bind(it) }
    private var listener: UploadListener? = null

    var pickedPhoto: Uri? = null
    var allPhotos = ArrayList<Uri>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    /*
    onAttach is for communication of fragments
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = if (context is UploadListener) {
            context
        } else {
            throw RuntimeException("$context must implement FirstListener")
        }
    }

    /*
    onDetach is for communication of fragments
     */
    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun initViews() {
        setViewHeight(binding.flView)
        binding.apply {
            ivPick.setOnClickListener {
                pickFishBunPhoto()
            }

            ivClose.setOnClickListener {
                hidePickedPhoto()
            }

            ivUpload.setOnClickListener {
                uploadNewPost()
            }
        }
    }

    private fun uploadNewPost() {
        listener!!.scrollToHome()
        val caption = binding.edtCaption.text.toString().trim()
        if (caption.isNotEmpty() && pickedPhoto != null) {
            uploadPostPhoto(caption, pickedPhoto!!)
        }
    }

    private fun uploadPostPhoto(caption: String, uri: Uri) {
        showLoading(requireActivity())
        StorageManager.uploadPostPhoto(uri, object : StorageHandler {
            override fun onSuccess(imgUrl: String) {
                val post = Post(caption, imgUrl)
                val uid = AuthManager.currentUser()!!.uid

                DatabaseManager.loadUser(uid, object : DBUserHandler {
                    override fun onSuccess(user: User?) {
                        post.uid = uid
                        post.fullname = user!!.fullname
                        post.userImg = user.userImg
                        storePostToDB(post)
                    }

                    override fun onError(e: Exception) {}
                })
            }

            override fun onError(exception: Exception?) {}

        })
    }

    private fun storePostToDB(post: Post) {
        DatabaseManager.storePosts(post, object : DBPostHandler {
            override fun onSuccess(post: Post) {
                storeFeedToDB(post)
            }

            override fun onError(e: Exception) {
                dismissLoading()
            }
        })
    }

    private fun storeFeedToDB(post: Post) {
        DatabaseManager.storeFeeds(post, object : DBPostHandler {
            override fun onSuccess(post: Post) {
                dismissLoading()
                resetAll()
               // listener!!.scrollToHome()
            }

            override fun onError(e: Exception) {

            }
        })
    }

    private fun resetAll() {
        binding.apply {
            allPhotos.clear()
            edtCaption.text.clear()
            pickedPhoto = null
            flPhoto.visibility = View.GONE
        }
    }

    private fun hidePickedPhoto() {
        pickedPhoto = null
        binding.flPhoto.visibility = View.GONE
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
            if (it.resultCode == Activity.RESULT_OK) {
                allPhotos =
                    it.data?.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf()
                pickedPhoto = allPhotos[0]
                showPickedPhoto()
            }
        }

    private fun showPickedPhoto() {
        binding.flPhoto.visibility = View.VISIBLE
        binding.ivPhoto.setImageURI(pickedPhoto)
    }

    /*
    Set view height as screen width
     */
    private fun setViewHeight(flView: FrameLayout) {
        val params: ViewGroup.LayoutParams = flView.layoutParams
        params.height = Utils.screenSize(requireActivity().application).width
        flView.layoutParams = params
    }

    /*
    This interface is created for communication with HomeFragment
     */
    interface UploadListener {
        fun scrollToHome()
    }
}