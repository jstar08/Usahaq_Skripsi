package com.example.usahaq_skripsi.ui.add.sales

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.usahaq_skripsi.databinding.ActivityCompleteTransactionBinding

class CompleteTransactionActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCompleteTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompleteTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val totalPrice = intent.getStringExtra(TOTALPRICE)

        binding.apply {

            tvTotal.text = totalPrice

            btnConfirmation.setOnClickListener {
                finish()
            }
        }
    }

    companion object{
        const val TOTALPRICE = "totalprice"
    }
}