package com.illicitintelligence.nippypay.view.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.illicitintelligence.nippypay.R
import com.illicitintelligence.nippypay.view.fragment.HomeFragmentAdapter
import kotlinx.android.synthetic.main.home_fragment_layout.*

class HomeFragment : Fragment(), ViewPager.OnPageChangeListener {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(
        R.layout.home_fragment_layout, container, false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        home_viewpager.adapter = HomeFragmentAdapter(childFragmentManager)
        home_viewpager.addOnPageChangeListener(this)
        home_bottom_navigationview.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.history_menu_item ->
                    home_viewpager.currentItem = 0
                R.id.make_payment_menu_item ->
                    home_viewpager.currentItem = 1
                else ->
                    home_viewpager.currentItem = 2
            }
            true
        }
        home_bottom_navigationview.selectedItemId = R.id.make_payment_menu_item
    }

    override fun onPageScrollStateChanged(state: Int) {
        //Do nothing
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        //Do nothing
    }

    override fun onPageSelected(position: Int) {
        when(position){
            0 -> home_bottom_navigationview.selectedItemId = R.id.history_menu_item
            1 -> home_bottom_navigationview.selectedItemId = R.id.make_payment_menu_item
            else -> home_bottom_navigationview.selectedItemId = R.id.settings_menu_item
        }
    }
}