package com.illicitintelligence.nippypay.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.illicitintelligence.nippypay.R
import com.illicitintelligence.nippypay.view.fragment.LoginFragment
import com.illicitintelligence.nippypay.view.fragment.home.HomeFragment

class BaseActivity : AppCompatActivity() {

    private val loginFragment: LoginFragment = LoginFragment()
    private val homeFragment: HomeFragment = HomeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        TODO: check of user has been logged in
        FirebaseAuth.getInstance().currentUser?.let {
            openHomeFragment()
        }?: showLoginFragment()
    }

    fun openHomeFragment() {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_animation,
                R.anim.slide_out_animation,
                R.anim.slide_in_animation,
                R.anim.slide_out_animation
            )
            .replace(R.id.home_frame_layout, homeFragment)
            .commit()
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
            .replace(R.id.home_frame_layout, loginFragment)
            .commit()
    }

    fun logoutUser() {

    }
}
