package com.illicitintelligence.nippypay.util

class Constants {
    companion object{
        val PAYMENT_MAP = mutableMapOf<Int, String>(Pair(0, "TNM Mpamba"), Pair(1, "Airtel Money"))
        val STATUS = "PROCESS_STATUS"
        val IN_PROGRESS = "IN_PROGRESS"
        val FAILED = "PAYMENT_FAILED"
        val SUCCESS = "PAYMENT_SUCCESS"
        val WRONG_PIN = "WRONG_PIN"
        val BALANCE = "INSUFFICIENT_BALANCE"
        val PAYMENT_OPTION = "PAYMENT_OPTION"
        val AIRTEL_MONEY = "AIRTEL_MONEY"
        val TNM_MPAMBA = "TNM_MPAMBA"
        val SHARED_PREF = "nippy.pay.pref"
    }
}