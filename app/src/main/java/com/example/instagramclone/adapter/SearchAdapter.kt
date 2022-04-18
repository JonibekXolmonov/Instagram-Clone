package com.example.instagramclone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.R
import com.example.instagramclone.databinding.ItemUserSearchBinding
import com.example.instagramclone.fragment.SearchFragment
import com.example.instagramclone.model.User

class SearchAdapter(val fragment: SearchFragment, var items: ArrayList<User>) : BaseAdapter() {

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
            holder.apply {
                itemUserSearchBinding.tvFullname.text = user.fullname
                itemUserSearchBinding.tvEmail.text = user.email
                Glide.with(itemUserSearchBinding.ivProfile.context)
                    .load(user.userImg)
                    .placeholder(R.drawable.img_default_user)
                    .error(R.drawable.img_default_user)
                    .into(holder.itemUserSearchBinding.ivProfile)

                itemUserSearchBinding.tvFollow.setOnClickListener {
                    if (!user.isFollowed) {
                        itemUserSearchBinding.tvFollow.text =
                            fragment.getString(R.string.str_following)
                    } else {
                        itemUserSearchBinding.tvFollow.text =
                            fragment.getString(R.string.str_follow)
                    }
                    fragment.followOrUnfollow(user)
                }

                if (!user.isFollowed) {
                    itemUserSearchBinding.tvFollow.text = fragment.getString(R.string.str_follow)
                } else {
                    itemUserSearchBinding.tvFollow.text = fragment.getString(R.string.str_following)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class UserViewHolder(var itemUserSearchBinding: ItemUserSearchBinding) :
        RecyclerView.ViewHolder(itemUserSearchBinding.root)
}