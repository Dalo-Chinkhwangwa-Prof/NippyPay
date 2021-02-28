package com.illicitintelligence.nippypay.service

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class PaymentAutomationService : AccessibilityService() {
    private lateinit var sharedPreferences: SharedPreferences

    private val WAIT_TIME: Long = 100
    override fun onInterrupt() {
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        sharedPreferences = getSharedPreferences("Dalo.Pay.Pref", Context.MODE_PRIVATE)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        sharedPreferences = getSharedPreferences("Dalo.Pay.Pref", Context.MODE_PRIVATE)
        sendBroadcast(Intent("from.payment.service").also {
            it.putExtra(STATUS, IN_PROGRESS)
        })

        if (sharedPreferences.getBoolean("Dalo.Pay.Enable", false)) {
            if (event.eventType == 32) {
                event.source?.let { source ->
                    when (sharedPreferences.getString(PAYMENT_OPTION, "NONE")) {
                        AIRTEL_MONEY -> airtelPayment(event, source)
                        TNM_MPAMBA -> tnmPayment(event, source)
                    }
                }
            }
        }
    }

    private fun tnmPayment(event: AccessibilityEvent, source: AccessibilityNodeInfo) {
        val message = event.text
        if (mmiCodeError(message.toString())
        ) {
            sendBroadcast(Intent("from.payment.service").also {
                it.putExtra(STATUS, FAILED)
            })
            source.findAccessibilityNodeInfosByText("OK").forEach {
                it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }

        } else if (invalidPin(message.toString())
        ) {
            sendBroadcast(Intent("from.payment.service").also {
                it.putExtra(STATUS, WRONG_PIN)
            })
            source.findAccessibilityNodeInfosByText("OK").forEach {
                it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        } else if (insufficientFunds(message.toString())
        ) {
            sendBroadcast(Intent("from.payment.service").also {
                it.putExtra(STATUS, BALANCE)
            })
            source.findAccessibilityNodeInfosByText("OK").forEach {
                it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        } else if (paymentSuccess(message.toString())
        ) {
            sendBroadcast(Intent("from.payment.service").also {
                it.putExtra(STATUS, SUCCESS)
            })
            source.findAccessibilityNodeInfosByText("OK").forEach {
                it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        } else if (systemBusy(message.toString())
        ) {
            sendBroadcast(Intent("from.payment.service").also {
                it.putExtra(STATUS, FAILED)
            })
            source.findAccessibilityNodeInfosByText("OK").forEach {
                it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        } else if (unregisteredReceiver(message.toString())) {
            sendBroadcast(Intent("from.payment.service").also {
                it.putExtra(STATUS, FAILED)
            })
            source.findAccessibilityNodeInfosByText("CANCEL").forEach {
                it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        } else if (event.text.isNotEmpty()) {

            when (sharedPreferences.getInt("currentStep", 0)) {
                0 -> {
                    if (isFirstMenuTNM(message.toString())) { //Some Phones are skipping initial loading
                        handleFirstMenuTNM(source)
                        skipStep()
                    } else
                        skipOnlyNonUSSD(message)
                }
                1 -> { //Select option #1 send money TNM
                    handleFirstMenuTNM(source)
                    skipOnlyNonUSSD(message)

                }
                2 -> { //Payment Option select TNM MPAMBA #1
                    val bundle = Bundle().also {
                        it.putCharSequence(
                            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                            "1"
                        )
                    }
                    val inputField = source.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)
                    inputField?.performAction(
                        AccessibilityNodeInfo.ACTION_SET_TEXT,
                        bundle
                    )
                    source.findAccessibilityNodeInfosByText("SEND").forEach {
                        Thread.sleep(WAIT_TIME)
                        it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    }
                    skipOnlyNonUSSD(message)
                }
                3 -> { //Choose to Send To Wallet #1
                    val bundle = Bundle().also {
                        it.putCharSequence(
                            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                            "1"
                        )
                    }
                    val inputField = source.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)
                    inputField?.performAction(
                        AccessibilityNodeInfo.ACTION_SET_TEXT,
                        bundle
                    )
                    source.findAccessibilityNodeInfosByText("SEND").forEach {
                        Thread.sleep(WAIT_TIME)
                        it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    }

                    skipOnlyNonUSSD(message)

                }
                4 -> {//Enter the phone number...
                    val phoneNumber =
                        sharedPreferences.getString("phoneNumber", "0000000000") ?: "0"
                    val bundle = Bundle().also {
                        it.putCharSequence(
                            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                            phoneNumber
                        )
                    }
                    val inputField = source.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)
                    inputField?.performAction(
                        AccessibilityNodeInfo.ACTION_SET_TEXT,
                        bundle
                    )
                    source.findAccessibilityNodeInfosByText("SEND").forEach {
                        Thread.sleep(WAIT_TIME)
                        it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    }

                    skipOnlyNonUSSD(message)

                }

                5 -> {//Enter Amount to send...
                    val sendAmount =
                        sharedPreferences.getString("sendAmount", "0") ?: "0"
                    val bundle = Bundle().also {
                        it.putCharSequence(
                            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                            sendAmount
                        )
                    }

                    val inputField = source.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)
                    inputField?.performAction(
                        AccessibilityNodeInfo.ACTION_SET_TEXT,
                        bundle
                    )
                    source.findAccessibilityNodeInfosByText("SEND").forEach {
                        Thread.sleep(WAIT_TIME)
                        it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    }
                    skipOnlyNonUSSD(message)
                }

                6 -> {//Confirm Transactio #1 Yes
                    val bundle = Bundle().also {
                        it.putCharSequence(
                            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                            "1"
                        )
                    }
                    val inputField = source.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)
                    inputField?.performAction(
                        AccessibilityNodeInfo.ACTION_SET_TEXT,
                        bundle
                    )
                    source.findAccessibilityNodeInfosByText("SEND").forEach {
                        Thread.sleep(WAIT_TIME)
                        it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    }
                    skipOnlyNonUSSD(message)
                }

                7 -> {// Enter your PIN TNM
                    val pinCode =
                        sharedPreferences.getString("pinCode", "1111") ?: "1111"
                    val bundle = Bundle().also {
                        it.putCharSequence(
                            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                            pinCode
                        )
                    }
                    val inputField = source.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)
                    inputField?.performAction(
                        AccessibilityNodeInfo.ACTION_SET_TEXT,
                        bundle
                    )
                    source.findAccessibilityNodeInfosByText("SEND").forEach {
                        Thread.sleep(WAIT_TIME)
                        it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    }
                    skipOnlyNonUSSD(message)
                }
            }
        }
    }

    private fun airtelPayment(
        event: AccessibilityEvent,
        source: AccessibilityNodeInfo
    ) {
        val message = event.text
        if (mmiCodeError(message.toString())) {
            sendBroadcast(Intent("from.payment.service").also {
                it.putExtra(STATUS, FAILED)
            })
            source.findAccessibilityNodeInfosByText("OK").forEach {
                it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }

        } else if (invalidPin(message.toString())
        ) {
            sendBroadcast(Intent("from.payment.service").also {
                it.putExtra(STATUS, WRONG_PIN)
            })
            source.findAccessibilityNodeInfosByText("OK").forEach {
                it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        } else if (insufficientFunds(message.toString())
        ) {

            sendBroadcast(Intent("from.payment.service").also {
                it.putExtra(STATUS, BALANCE)
            })
            source.findAccessibilityNodeInfosByText("OK").forEach {
                it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        } else if (paymentSuccess(message.toString())
        ) {
            sendBroadcast(Intent("from.payment.service").also {
                it.putExtra(STATUS, SUCCESS)
            })
            source.findAccessibilityNodeInfosByText("OK").forEach {
                it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        } else if (systemBusy(message.toString())
        ) {
            sendBroadcast(Intent("from.payment.service").also {
                it.putExtra(STATUS, FAILED)
            })
            source.findAccessibilityNodeInfosByText("OK").forEach {
                it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        } else if (event.text.isNotEmpty()) {
            when (sharedPreferences.getInt("currentStep", 0)) {
                0 -> {
                    if (isFirstMenuAirtel(message.toString())) { //Some Phones are skipping initial loading
                        handleFirstAirtelMenu(source)
                        skipStep()
                    } else
                        skipOnlyNonUSSD(message)
                }
                1 -> { //Select option #2 send money
                    handleFirstAirtelMenu(source)
                    skipOnlyNonUSSD(message)

                }
                2 -> { //Payment Option select Airtel number #1

                    val bundle = Bundle().also {
                        it.putCharSequence(
                            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                            "1"
                        )
                    }
                    val inputField = source.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)
                    inputField?.performAction(
                        AccessibilityNodeInfo.ACTION_SET_TEXT,
                        bundle
                    )
                    source.findAccessibilityNodeInfosByText("SEND").forEach {
                        Thread.sleep(WAIT_TIME)
                        it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    }
                    skipOnlyNonUSSD(message)
                }
                3 -> { //Choose to enter phone number #1

                    val bundle = Bundle().also {
                        it.putCharSequence(
                            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                            "1"
                        )
                    }
                    val inputField = source.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)
                    inputField?.performAction(
                        AccessibilityNodeInfo.ACTION_SET_TEXT,
                        bundle
                    )
                    source.findAccessibilityNodeInfosByText("SEND").forEach {
                        Thread.sleep(WAIT_TIME)
                        it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    }
                    skipOnlyNonUSSD(message)
                }
                4 -> {//Enter the phone number...

                    val phoneNumber =
                        sharedPreferences.getString("phoneNumber", "0000000000") ?: "0"
                    val bundle = Bundle().also {
                        it.putCharSequence(
                            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                            phoneNumber
                        )
                    }
                    val inputField = source.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)
                    inputField?.performAction(
                        AccessibilityNodeInfo.ACTION_SET_TEXT,
                        bundle
                    )
                    source.findAccessibilityNodeInfosByText("SEND").forEach {
                        Thread.sleep(WAIT_TIME)
                        it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    }

                    skipOnlyNonUSSD(message)

                }

                5 -> {//Enter Amount to send...

                    val sendAmount =
                        sharedPreferences.getString("sendAmount", "0") ?: "0"
                    val bundle = Bundle().also {
                        it.putCharSequence(
                            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                            sendAmount
                        )
                    }
                    val inputField = source.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)
                    inputField?.performAction(
                        AccessibilityNodeInfo.ACTION_SET_TEXT,
                        bundle
                    )
                    source.findAccessibilityNodeInfosByText("SEND").forEach {
                        Thread.sleep(WAIT_TIME)
                        it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    }
                    skipOnlyNonUSSD(message)
                }

                6 -> {//Enter pincode

                    val pinCode =
                        sharedPreferences.getString("pinCode", "1111") ?: "1111"
                    val bundle = Bundle().also {
                        it.putCharSequence(
                            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                            pinCode
                        )
                    }
                    val inputField = source.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)
                    inputField?.performAction(
                        AccessibilityNodeInfo.ACTION_SET_TEXT,
                        bundle
                    )
                    source.findAccessibilityNodeInfosByText("SEND").forEach {
                        Thread.sleep(WAIT_TIME)
                        it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    }
                    skipOnlyNonUSSD(message)
                }

                7 -> {// Do you want to mark as fav number

                    val bundle = Bundle().also {
                        it.putCharSequence(
                            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                            "2"
                        )
                    }
                    val inputField = source.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)
                    inputField?.performAction(
                        AccessibilityNodeInfo.ACTION_SET_TEXT,
                        bundle
                    )
                    source.findAccessibilityNodeInfosByText("SEND").forEach {
                        Thread.sleep(WAIT_TIME)
                        it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    }
                    skipOnlyNonUSSD(message)
                }
            }
        }
    }

    private fun skipOnlyNonUSSD(message: MutableList<CharSequence>) {

        if (!isUssdRunningMessage(message.toString()))
            incrementSteps()
    }

    fun isUssdRunningMessage(message: String) =
        ((message.contains("USSD", true) && message
            .contains("code", true) && message.contains(
            "running",
            true
        )) || (message.contains("[Phone", true)))

    private fun handleFirstMenuTNM(source: AccessibilityNodeInfo) {
        val bundle = Bundle().also {
            it.putCharSequence(
                AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                "1"
            )
        }
        val inputField = source.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)
        inputField?.performAction(
            AccessibilityNodeInfo.ACTION_SET_TEXT,
            bundle
        )

        source.findAccessibilityNodeInfosByText("SEND").forEach {
            Thread.sleep(WAIT_TIME)
            it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        }
    }

    private fun handleFirstAirtelMenu(source: AccessibilityNodeInfo) {
        val bundle = Bundle().also {
            it.putCharSequence(
                AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                "2"
            )
        }
        val inputField = source.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)

        inputField?.performAction(
            AccessibilityNodeInfo.ACTION_SET_TEXT,
            bundle
        )

        source.findAccessibilityNodeInfosByText("SEND").forEach {
            Thread.sleep(WAIT_TIME)
            it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        }
    }

    private fun isFirstMenuAirtel(message: String) =
        message.contains("Buy Airtime", true) || message.contains(
            "Make Payments",
            true
        ) || message.contains("Withdraw Money", true)

    private fun isFirstMenuTNM(message: String) =
        message.contains(
            "Main Menu",
            true
        ) || message.contains("Send Money") || message.contains("Financial Services")

    fun systemBusy(message: String): Boolean {
        return message.isNotEmpty() && ((message
            .contains("system", true) && message
            .contains("busy", true)))
    }

    fun mmiCodeError(message: String): Boolean {
        return message.isNotEmpty() && (message
            .contains("invalid", true) || (message
            .contains("invalid", true) && message
            .contains("MMI", true)) || (message
            .contains("non", true) && message
            .contains("existing", true)))
    }

    fun invalidPin(message: String): Boolean {
        return message.isNotEmpty() && ((message
            .contains("PIN", true)) && (message
            .contains("wrong", true) || message
            .contains("invalid", true)))
    }

    fun insufficientFunds(message: String): Boolean {
        return message.isNotEmpty() && (((message
            .contains("amount", true) || message
            .contains("balance", true) || message.contains("funds", true)) && message
            .contains("insufficient", true) || message.contains("insuffient", true)))
    }

    fun unregisteredReceiver(message: String): Boolean {
        return message.isNotEmpty() && (((message
            .contains("unregistered", true) && message
            .contains("account", true))))
    }

    fun paymentSuccess(message: String): Boolean {
        return message.isNotEmpty() && ((message
            .contains("request", true) && (message
            .contains("processed", true) || message
            .contains("procesed", true))) ||
                (message
                    .contains("money", true) && (message
                    .contains("sent", true))))
    }

    private fun incrementSteps() {
        sharedPreferences.edit()
            .putInt("currentStep", sharedPreferences.getInt("currentStep", 0) + 1).apply()
    }

    private fun skipStep() {
        sharedPreferences.edit()
            .putInt("currentStep", sharedPreferences.getInt("currentStep", 0) + 2).apply()
    }
}
