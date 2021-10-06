package com.example.usahaq_skripsi.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.usahaq_skripsi.databinding.ActivityLoginBinding
import com.example.usahaq_skripsi.ui.dashboard.DashboardActivity
import com.example.usahaq_skripsi.ui.register.Register
import com.example.usahaq_skripsi.util.ViewModelFactory
import com.example.usahaq_skripsi.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var viewModel : AuthViewModel
    private lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        reload()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        binding.apply {
            btnLogin.setOnClickListener {
                val email  = etUsername.text.toString()
                val password = etPassword.text.toString()
                if(etUsername.text.trim().isEmpty() && etPassword.text.trim().isEmpty()){
                    etUsername.error = "This field can't be empty"
                    etPassword.error = "This field can't be empty"
                } else if(etUsername.text.trim().isEmpty()) {
                    etUsername.error = "This field can't be empty"
                } else if(etPassword.text.trim().isEmpty()){
                    etPassword.error = "This field can't be empty"
                } else {
                    viewModel.signIn(email , password)
                }
            }

            tvSignUp.setOnClickListener {
                startActivity(Intent(this@Login, Register::class.java))
            }
        }
    }

    private fun reload() {
        if (auth.currentUser != null) {
            startActivity(Intent(this, DashboardActivity::class.java))
        }
    }
}