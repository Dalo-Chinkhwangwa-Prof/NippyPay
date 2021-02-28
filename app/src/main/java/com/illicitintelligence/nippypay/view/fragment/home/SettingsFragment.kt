package com.illicitintelligence.nippypay.view.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.illicitintelligence.nippypay.R
import com.illicitintelligence.nippypay.view.activity.BaseActivity
import kotlinx.android.synthetic.main.profile_fragment_layout.*

class SettingsFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.profile_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sign_out_button.setOnClickListener {
            context?.let {
                (it as BaseActivity).logoutUser()
            }
        }
    }
}