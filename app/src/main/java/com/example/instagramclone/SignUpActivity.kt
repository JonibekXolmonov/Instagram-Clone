package com.example.instagramclone

import android.os.Bundle
import com.example.instagramclone.databinding.ActivitySignUpBinding
import com.example.instagramclone.manager.handler.AuthHandler
import com.example.instagramclone.manager.AuthManager
import com.example.instagramclone.manager.DatabaseManager
import com.example.instagramclone.manager.PrefsManager
import com.example.instagramclone.manager.handler.DBUserHandler
import com.example.instagramclone.model.User
import com.example.instagramclone.utils.Extensions.toast
import java.lang.Exception

/*
In SignUpActivity user can sign up after entering fullname,email,password
 */
class SignUpActivity : BaseActivity() {

    lateinit var binding: ActivitySignUpBinding
    val TAG = SignUpActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        binding.apply {
            btnSignUp.setOnClickListener {
                val fullname = edtFullname.text.toString()
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()
                val deviceToken = PrefsManager(this@SignUpActivity).loadDeviceToken()!!

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    val user = User(fullname, email, password, "")
                    user.deviceTokens.add(deviceToken)
                    firebaseSignUp(user)
                } else {
                    toast(getString(R.string.str_signup_empty))
                }
            }

            tvSignIn.setOnClickListener {
                finish()
            }
        }
    }

    private fun firebaseSignUp(user: User) {
        showLoading(this)
        AuthManager.signUp(user.email, user.password, object : AuthHandler {
            override fun onSuccess(uid: String) {
                user.uid = uid
                storeUserToDB(user)
            }

            override fun onError(exception: Exception?) {
                dismissLoading()
                toast(getString(R.string.str_signup_failed))
            }
        })
    }

    private fun storeUserToDB(user: User) {
        DatabaseManager.storeUser(user, object : DBUserHandler {
            override fun onSuccess(user: User?) {
                dismissLoading()
                toast(getString(R.string.str_signup_success))
                callMainActivity(context!!)
            }

            override fun onError(e: Exception) {}
        })
    }
}