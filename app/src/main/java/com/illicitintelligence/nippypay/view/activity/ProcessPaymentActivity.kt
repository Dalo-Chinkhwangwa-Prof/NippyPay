package com.illicitintelligence.nippypay.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.illicitintelligence.nippypay.R
import com.illicitintelligence.nippypay.model.PaymentPackage

class ProcessPaymentActivity : AppCompatActivity() {

    companion object{
        const val PAYMENT_PACKAGE_KEY = "PAYMENT_PACKAGE"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_process_payment)

        val paymentPackage = intent.getParcelableExtra<PaymentPackage>(PAYMENT_PACKAGE_KEY)
        paymentPackage?.let {
            //TODO: Start payment services...
        }
    }
}