package com.illicitintelligence.nippypay.view.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.illicitintelligence.nippypay.view.fragment.home.ActivityFragment
import com.illicitintelligence.nippypay.view.fragment.home.NewPaymentFragment
import com.illicitintelligence.nippypay.view.fragment.home.SettingsFragment

class HomeFragmentAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    companion object{
        const val FRAGMENT_COUNT = 3
    }
    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> ActivityFragment()
            1 -> NewPaymentFragment()
            else -> SettingsFragment()
        }
    }
    override fun getCount(): Int = FRAGMENT_COUNT
}