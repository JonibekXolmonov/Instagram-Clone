package com.example.instagramclone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.databinding.ItemPostProfileBinding
import com.example.instagramclone.model.Post
import com.example.instagramclone.utils.Utils

class ProfileAdapter(val fragment: Fragment, var items: ArrayList<Post>) : BaseAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PostViewHolder(
            ItemPostProfileBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post = items[position]
        if (holder is PostViewHolder) {
            setViewHeight(holder.itemPostProfileBinding.ivPost)
            Glide.with(fragment).load(post.postImage).into(holder.itemPostProfileBinding.ivPost)
            holder.itemPostProfileBinding.tvCaption.text = post.caption
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class PostViewHolder(val itemPostProfileBinding: ItemPostProfileBinding) :
        RecyclerView.ViewHolder(itemPostProfileBinding.root)

    /*
    * Set ShapeableImageView height as screen width
    * */

    private fun setViewHeight(view: View) {
        val params: ViewGroup.LayoutParams = view.layoutParams
        params.height = Utils.screenSize(fragment.requireActivity().application).width / 2
        view.layoutParams = params
    }

}