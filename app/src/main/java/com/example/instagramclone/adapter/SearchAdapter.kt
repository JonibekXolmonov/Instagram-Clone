package com.example.instagramclone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.R
import com.example.instagramclone.databinding.ItemUserSearchBinding
import com.example.instagramclone.model.User

class SearchAdapter(var items: ArrayList<User>) : BaseAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UserViewHolder(
            ItemUserSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val user: User = items[position]
        if (holder is UserViewHolder) {
            holder.itemUserSearchBinding.tvFullname.text = user.fullname
            holder.itemUserSearchBinding.tvEmail.text = user.email
            Glide.with(holder.itemUserSearchBinding.ivProfile.context)
                .load(user.userImg)
                .placeholder(R.drawable.img_default_user)
                .error(R.drawable.img_default_user)
                .into(holder.itemUserSearchBinding.ivProfile)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class UserViewHolder(var itemUserSearchBinding: ItemUserSearchBinding) :
        RecyclerView.ViewHolder(itemUserSearchBinding.root)
}