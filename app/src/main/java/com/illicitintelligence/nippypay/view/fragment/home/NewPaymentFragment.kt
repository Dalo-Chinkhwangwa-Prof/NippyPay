package com.illicitintelligence.nippypay.view.fragment.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.illicitintelligence.nippypay.R
import com.illicitintelligence.nippypay.util.Constants.Companion.PAYMENT_MAP
import kotlinx.android.synthetic.main.new_payment_layout.*
import kotlinx.android.synthetic.main.new_payment_layout.view.*

class NewPaymentFragment : Fragment() {

    private var sendToNumber = "[phone]"
    private var amount = 0
    private var paymentOption = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.new_payment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tnm_option_item.isChecked = true
        setAssuranceText()

        store_number.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                sendToNumber = s.toString()
                setAssuranceText()
            }
        })
        amount_edittext.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
//                TODO("Not yet implemented")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                amount = Integer.parseInt(s.toString())
                setAssuranceText()
            }
        })
        payment_option_group.setOnCheckedChangeListener { group, checkedId ->

            when(checkedId){
                R.id.tnm_option_item ->{
                    paymentOption = 0
                    setAssuranceText()
                }
                else -> {
                    paymentOption = 1
                    setAssuranceText()
                }
            }
        }
        payment_button.setOnClickListener {

        }

    }

    private fun setAssuranceText() {
        assure_textview.text =
            getString(R.string.payment_assurance, amount, sendToNumber, PAYMENT_MAP[paymentOption])
    }
}