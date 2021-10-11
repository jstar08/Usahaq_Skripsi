package com.example.usahaq_skripsi.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.usahaq_skripsi.databinding.ActivityAddPurchaseBinding

class AddPurchaseActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddPurchaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}