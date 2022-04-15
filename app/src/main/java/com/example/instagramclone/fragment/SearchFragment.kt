package com.example.instagramclone.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.instagramclone.manager.DatabaseManager
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
        binding.rvSearch.adapter = SearchAdapter(items)
    }

    private fun loadUsers() {
        DatabaseManager.loadUsers(object : DBUsersHandler {
            override fun onSuccess(users: ArrayList<User>) {
                items.clear()
                items.addAll(users)
                refreshAdapter(items)
            }

            override fun onError(e: Exception) {

            }
        })
    }
}