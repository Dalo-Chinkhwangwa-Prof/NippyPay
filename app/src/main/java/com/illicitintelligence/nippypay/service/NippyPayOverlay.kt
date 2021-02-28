package com.illicitintelligence.nippypay.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.WindowManager

class NippyPayOverlay: Service() {
//    private lateinit var paymentLoadView: PaymentLoadView
    private lateinit var wm: WindowManager

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        paymentLoadView = PaymentLoadView(this@OverlayService)
        wm = this@NippyPayOverlay.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val parameters = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT
            )
        } else {
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT
            )
        }

        parameters.x = 0
        parameters.y = 0
        parameters.gravity = Gravity.CENTER or Gravity.CENTER
        //windowManager.add

        try {
            wm.addView(paymentLoadView, parameters)
        } catch (e: Exception) {
            Log.d("TAG_X", e.toString())
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        wm.removeViewImmediate(paymentLoadView)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}