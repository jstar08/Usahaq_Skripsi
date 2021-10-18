package com.example.usahaq_skripsi.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.usahaq_skripsi.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}