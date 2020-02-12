package com.illicitintelligence.nippypay.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.illicitintelligence.nippypay.R
import kotlinx.android.synthetic.main.login_fragment_layout.*

class LoginFragment : Fragment() {

    val signUpFragment: SignUpFragment = SignUpFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.login_fragment_layout, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpClickListeners()
    }

    private fun setUpClickListeners() {

        signup_textview.setOnClickListener {
            childFragmentManager
                .beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_animation,
                    R.anim.slide_out_animation,
                    R.anim.slide_in_animation,
                    R.anim.slide_out_animation
                )
                .add(R.id.signup_framelayout, signUpFragment)
                .addToBackStack(signUpFragment.tag)
                .commit()
        }

        login_button.setOnClickListener {

        }

    }
}