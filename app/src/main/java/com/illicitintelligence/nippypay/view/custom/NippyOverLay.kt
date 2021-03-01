package com.illicitintelligence.nippypay.view.custom

import android.content.Context
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.illicitintelligence.nippypay.R

class NippyOverLay(context: Context): ConstraintLayout(context) {

    private var inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    init {

        inflater.inflate(R.layout.nippy_overlay_layout, this, true)
    }
}