package com.illicitintelligence.nippypay.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.illicitintelligence.nippypay.R
import com.illicitintelligence.nippypay.view.fragment.LoginFragment

class BaseActivity : AppCompatActivity() {

    private val loginFragment: LoginFragment = LoginFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        TODO: check of user has been logged in
        showLoginFragment()
    }

    private fun showLoginFragment() {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_animation,
                R.anim.slide_out_animation,
                R.anim.slide_in_animation,
                R.anim.slide_out_animation
            )
            .add(R.id.home_frame_layout, loginFragment)
            .commit()
    }


    fun loginUser() {

    }

    fun logoutUser() {

    }

    fun signUpUser() {

    }

}
