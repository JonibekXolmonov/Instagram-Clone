package com.example.instagramclone

import android.content.Intent
import android.os.Bundle
import com.example.instagramclone.databinding.ActivitySignInBinding
import com.example.instagramclone.manager.handler.AuthHandler
import com.example.instagramclone.manager.AuthManager
import com.example.instagramclone.manager.DatabaseManager
import com.example.instagramclone.manager.PrefsManager
import com.example.instagramclone.manager.handler.DBUserHandler
import com.example.instagramclone.model.User
import com.example.instagramclone.utils.Extensions.toast
import java.lang.Exception

/*
In SignInActivity, user cal login using email, password
 */
class SignInActivity : BaseActivity() {

    lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {

        binding.apply {
            btnSignIn.setOnClickListener {
                if (edtEmail.text.isNotEmpty() && edtPassword.text.isNotEmpty()) {
                    firebaseSignIn(
                        email = edtEmail.text.toString(),
                        password = edtPassword.text.toString()
                    )
                } else {
                    toast(getString(R.string.str_signin_empty))
                }
            }

            tvSignUp.setOnClickListener {
                callSignUpActivity()
            }
        }
    }

    private fun firebaseSignIn(email: String, password: String) {
        showLoading(this)
        AuthManager.signIn(email, password, object : AuthHandler {
            override fun onSuccess(uid: String) {
                dismissLoading()
                toast(getString(R.string.str_signin_success))
                storeDeviceTokenToUser()
            }

            override fun onError(exception: Exception?) {
                dismissLoading()
                toast(getString(R.string.str_signin_failed))
            }
        })
    }

    private fun storeDeviceTokenToUser() {
        val deviceToken = PrefsManager(this).loadDeviceToken()
        var uid = AuthManager.currentUser()!!.uid
        DatabaseManager.addMyDeviceToken(uid, deviceToken, object : DBUserHandler {
            override fun onSuccess(user: User?) {
                callMainActivity(context!!)
            }

            override fun onError(e: Exception) {
                callMainActivity(context!!)
            }

        })
    }

    private fun callSignUpActivity() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
}