package com.example.instagramclone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.R
import com.example.instagramclone.databinding.ItemPostHomeBinding
import com.example.instagramclone.model.Post

class FavoriteAdapter(var items: ArrayList<Post>) : BaseAdapter() {
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
            Glide.with(holder.itemPostHomeBinding.ivPost)
                .load(post.image)
                .into(holder.itemPostHomeBinding.ivPost)

            holder.itemPostHomeBinding.apply {
                ivLike.setOnClickListener {
                    ivLike.setImageResource(R.drawable.ic_favorite_filled)
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    inner class PostViewHolder(var itemPostHomeBinding: ItemPostHomeBinding) :
        RecyclerView.ViewHolder(itemPostHomeBinding.root)
}