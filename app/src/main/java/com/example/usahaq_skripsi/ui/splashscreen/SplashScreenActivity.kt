package com.example.usahaq_skripsi.ui.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.usahaq_skripsi.databinding.ActivitySplashScreenBinding
import com.example.usahaq_skripsi.ui.intro.AppIntroActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySplashScreenBinding

    private val splashDuration = 1500L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(mainLooper).postDelayed({

            startActivity(Intent(this, AppIntroActivity::class.java))
        }, splashDuration)
    }
}