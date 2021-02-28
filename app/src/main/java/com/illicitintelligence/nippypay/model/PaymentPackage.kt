package com.illicitintelligence.nippypay.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PaymentPackage(val sendTo: Int, val paymentOption: Int, val pinCode: Int, val amount: Int) :
    Parcelable