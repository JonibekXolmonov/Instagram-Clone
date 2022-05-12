package com.example.instagramclone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.R
import com.example.instagramclone.databinding.ItemPostHomeBinding
import com.example.instagramclone.fragment.FavoriteFragment
import com.example.instagramclone.model.Post

class FavoriteAdapter(val fragment: FavoriteFragment, var items: ArrayList<Post>) : BaseAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PostViewHolder(
            ItemPostHomeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post = items[position]
        if (holder is PostViewHolder) {
            holder.itemPostHomeBinding.apply {
                tvFullname.text = post.fullname
                tvCaption.text = post.caption
                tvTime.text = post.currentDate
                Glide.with(fragment)
                    .load(post.userImg)
                    .placeholder(R.drawable.img_default_user)
                    .error(R.drawable.img_default_user)
                    .into(ivProfile)

                Glide.with(fragment)
                    .load(post.postImage)
                    .into(ivPost)

                if (post.isLiked) {
                    ivLike.setImageResource(R.drawable.ic_favorite_filled)
                } else {
                    ivLike.setImageResource(R.drawable.ic_favorite)
                }

                ivLike.setOnClickListener {
                    if (post.isLiked) {
                        post.isLiked = false
                        ivLike.setImageResource(R.drawable.ic_favorite)
                    } else {
                        post.isLiked = true
                        ivLike.setImageResource(R.drawable.ic_favorite_filled)
                    }
                    fragment.likeOrUnlikePost(post)
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    inner class PostViewHolder(var itemPostHomeBinding: ItemPostHomeBinding) :
        RecyclerView.ViewHolder(itemPostHomeBinding.root)
}