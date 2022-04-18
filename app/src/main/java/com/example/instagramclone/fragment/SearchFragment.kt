package com.example.instagramclone.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.instagramclone.R
import com.example.instagramclone.adapter.SearchAdapter
import com.example.instagramclone.databinding.FragmentHomeBinding
import com.example.instagramclone.databinding.FragmentSearchBinding
import com.example.instagramclone.extension.viewBinding
import com.example.instagramclone.manager.AuthManager
import com.example.instagramclone.manager.DatabaseManager
import com.example.instagramclone.manager.handler.DBFollowHandler
import com.example.instagramclone.manager.handler.DBUserHandler
import com.example.instagramclone.manager.handler.DBUsersHandler
import com.example.instagramclone.model.User
import java.lang.Exception


class SearchFragment : BaseFragment() {

    private val binding by viewBinding { FragmentSearchBinding.bind(it) }
    var items = ArrayList<User>()
    var users = ArrayList<User>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
    }

    private fun initView() {


        binding.apply {
            edtSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val keyWord = s.toString().trim()
                    usersByKeyWord(keyWord)
                }

                override fun afterTextChanged(s: Editable?) {}

            })

            loadUsers()
            refreshAdapter(items)
        }
    }

    private fun usersByKeyWord(keyWord: String) {
        if (keyWord.isEmpty()) {
            refreshAdapter(items)
        }

        users.clear()
        for (user in items) {
            if (user.fullname.lowercase().startsWith(keyWord.lowercase()))
                users.add(user)
        }

        refreshAdapter(users)
    }

    private fun refreshAdapter(items: ArrayList<User>) {
        binding.rvSearch.adapter = SearchAdapter(this, items)
    }

    private fun loadUsers() {
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.loadUsers(object : DBUsersHandler {
            override fun onSuccess(users: ArrayList<User>) {
                DatabaseManager.loadFollowing(uid, object : DBUsersHandler {
                    override fun onSuccess(following: ArrayList<User>) {
                        items.clear()
                        items.addAll(mergedUsers(uid, users, following))
                        Log.d("TAG", "onSuccess: $items")
                        refreshAdapter(items)
                    }

                    override fun onError(e: Exception) {}
                })
            }

            override fun onError(e: Exception) {

            }
        })
    }

    private fun mergedUsers(uid: String, users: ArrayList<User>, following: ArrayList<User>): ArrayList<User> {
        val items = ArrayList<User>()
        for (u in users) {
            val user = u
            for (f in following) {
                if (u.uid == f.uid) {
                    user.isFollowed = true
                    break
                }
            }
            if (uid != user.uid) {
                items.add(user)
            }
        }
        return items
    }

    fun followOrUnfollow(to: User) {
        val uid = AuthManager.currentUser()!!.uid
        if (!to.isFollowed) {
            followUser(uid, to)
        } else {
            unFollowUser(uid, to)
        }
    }

    private fun unFollowUser(uid: String, to: User) {
        DatabaseManager.loadUser(uid, object : DBUserHandler {
            override fun onSuccess(me: User?) {
                DatabaseManager.unFollowUser(me!!, to, object : DBFollowHandler {
                    override fun onSuccess(isDone: Boolean) {
                        to.isFollowed = false
                        DatabaseManager.removePostsToMyFeed(uid, to)
                    }

                    override fun onError(e: Exception) {}
                })
            }

            override fun onError(e: Exception) {}
        })
    }

    private fun followUser(uid: String, to: User) {
        DatabaseManager.loadUser(uid, object : DBUserHandler {
            override fun onSuccess(me: User?) {
                DatabaseManager.followUser(me!!, to, object : DBFollowHandler {
                    override fun onSuccess(isDone: Boolean) {
                        to.isFollowed = true
                        DatabaseManager.storePostsToMyFeed(uid, to)
                    }

                    override fun onError(e: Exception) {}
                })
            }

            override fun onError(e: Exception) {}
        })
    }
}