package com.example.usahaq_skripsi.ui.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import com.example.usahaq_skripsi.databinding.ActivitySettingBinding
import com.example.usahaq_skripsi.ui.login.Login
import com.google.firebase.auth.FirebaseAuth

class SettingActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.apply {
            clLanguage.setOnClickListener {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            clLogout.setOnClickListener {
                auth.signOut()
                startActivity(Intent(this@SettingActivity, Login::class.java))
                finish()
            }
            btnBack.setOnClickListener {
                onBackPressed()
            }
        }
    }
}