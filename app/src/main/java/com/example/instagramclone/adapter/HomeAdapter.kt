package com.example.instagramclone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.R
import com.example.instagramclone.databinding.ItemPostHomeBinding
import com.example.instagramclone.fragment.HomeFragment
import com.example.instagramclone.model.Post

class HomeAdapter(val fragment: HomeFragment, var items: ArrayList<Post>) : BaseAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PostViewHolder(
            ItemPostHomeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    lateinit var onMoreClick: ((postImage: String) -> Unit)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post = items[position]
        if (holder is PostViewHolder) {
            holder.itemPostHomeBinding.apply {
                tvCaption.text = post.caption
                tvTime.text = post.currentDate
                tvFullname.text = post.fullname
                Glide.with(ivProfile)
                    .load(post.userImg)
                    .into(ivProfile)

                Glide.with(ivPost)
                    .load(post.postImage)
                    .placeholder(R.drawable.loading)
                    .into(ivPost)

                ivLike.setOnClickListener {
                    if (post.isLiked) {
                        post.isLiked = false
                        ivLike.setImageResource(R.drawable.ic_favorite)
                    } else {
                        post.isLiked = true
                        ivLike.setImageResource(R.drawable.ic_favorite_filled)
                    }
                    fragment.likeOrUnlikePost(post)

                    ivPost.setOnClickListener {
                        onMoreClick.invoke(post.postImage)
                    }
                }
                if (post.isLiked) {
                    ivLike.setImageResource(R.drawable.ic_favorite_filled)
                } else {
                    ivLike.setImageResource(R.drawable.ic_favorite)
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    inner class PostViewHolder(var itemPostHomeBinding: ItemPostHomeBinding) :
        RecyclerView.ViewHolder(itemPostHomeBinding.root) {
    }
}