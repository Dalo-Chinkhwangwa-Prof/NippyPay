package com.illicitintelligence.nippypay.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.illicitintelligence.nippypay.R
import com.illicitintelligence.nippypay.view.activity.BaseActivity
import kotlinx.android.synthetic.main.login_fragment_layout.*
import java.util.concurrent.TimeUnit

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
        login_cardview.animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_animation)
    }

    private fun setUpClickListeners() {

//        signup_textview.setOnClickListener {
//            childFragmentManager
//                .beginTransaction()
//                .setCustomAnimations(
//                    R.anim.slide_in_animation,
//                    R.anim.slide_out_animation,
//                    R.anim.slide_in_animation,
//                    R.anim.slide_out_animation
//                )
//                .add(R.id.signup_framelayout, signUpFragment)
//                .addToBackStack(signUpFragment.tag)
//                .commit()
//        }

        login_button.setOnClickListener {
            val phoneNumber = "+265${phone_number_edittext.text.toString().substring(1, 10)}"
            Log.d("TAG_X", "On the way")
            loading_spinner.visibility = View.VISIBLE
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                requireActivity(),
                object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                    override fun onCodeSent(
                        verificationID: String,
                        p1: PhoneAuthProvider.ForceResendingToken
                    ) {
                        super.onCodeSent(verificationID, p1)
                        loading_spinner.visibility = View.GONE
                        verification_code_edittext.visibility = View.VISIBLE
                        showVerificationSentMessage()
                        Log.d("TAG_X", "Message sent")
                        login_button.text = getString(R.string.verify_text)
                        verification_code_edittext.visibility = View.VISIBLE
                        login_button.setOnClickListener {
                            verifyCode(verificationID)
                        }
                    }
                    override fun onVerificationCompleted(phoneAuth: PhoneAuthCredential) {
                        signInWithPhoneCredential(phoneAuth)
                        loading_spinner.visibility = View.GONE
                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        loading_spinner.visibility = View.GONE
                        p0.localizedMessage?.let {
                            showLogErrorMessage(it)
                        } ?: showLogErrorMessage(getString(R.string.unknown_error_message))

                    }

                }
            )
        }

    }

    private fun showLogErrorMessage(error: String) {
        MaterialAlertDialogBuilder(activity)
            .setTitle(getString(R.string.phone_verification_title))
            .setMessage(error)
            .setPositiveButton(getString(R.string.okay_text)) { dialog, _ ->
                dialog.dismiss()
            }.create()
            .show()
    }

    private fun signInWithPhoneCredential(phoneAuth: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuth)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    (activity as BaseActivity).openHomeFragment()
                } else {
                    Log.d("TAG_X", "Error ${task.exception?.localizedMessage}")
                    showLogErrorMessage(getString(R.string.sign_in_error))
                }
            }
    }

    private fun showVerificationSentMessage() {
        MaterialAlertDialogBuilder(activity)
            .setTitle(getString(R.string.phone_verification_title))
            .setMessage(getString(R.string.phone_verification_message))
            .setPositiveButton(getString(R.string.okay_text)) { dialog, _ ->
                dialog.dismiss()
            }.create()
            .show()
    }

    private fun verifyCode(verificationID: String) {
        Log.d("TAG_X", verificationID)
        if (verification_code_edittext.text.toString()
                .isNotEmpty() && phone_number_edittext.text.toString().isNotEmpty()
        ) {
            val phoneAuthCredential = PhoneAuthProvider.getCredential(
                verificationID,
                verification_code_edittext.text.toString().trim()
            )
            signInWithPhoneCredential(phoneAuthCredential)
        }
    }
}

/*
* if (loginValid()) {
            loading_spinner.setLoadingText(getString(R.string.signing_in))
            var phoneNumber = user_phone_edittext.text.toString().trim()
            phoneNumber = "+265${phoneNumber.substring(1, phoneNumber.length)}"
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                requireActivity(),
                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onCodeSent(
                        verificationCode: String,
                        p1: PhoneAuthProvider.ForceResendingToken
                    ) {
                        super.onCodeSent(verificationCode, p1)
                        context?.let {
                            displayErrorDialog(
                                it.getString(R.string.verification_code_title),
                                it.getString(R.string.verficiation_message)
                            )
                        }
                        user_code_edittext_layout.visibility = View.VISIBLE
                        login_button.text = (getString(R.string.verify_text))
                        login_button.setOnClickListener {
                            if (user_code_edittext_layout.visibility == View.VISIBLE)
                                verifyCode(verificationCode)
                            else
                                loginUser()
                        }
                    }

                    override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                        signInWithPhoneCredentials(phoneAuthCredential)
                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        Log.d("TAG_ERROR", p0.message + "")
                        context?.let {
                            displayErrorDialog(
                                it.getString(R.string.verification_error),
                                it.getString(R.string.invalid_phone_string)
                            )
                        }
                    }
                }
            )
        }
* */