package com.example.usahaq_skripsi.ui.register.registerpage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.usahaq_skripsi.R
import com.example.usahaq_skripsi.databinding.FragmentRegisterPage1Binding
import com.example.usahaq_skripsi.listener.RegisterActivityListener
import com.example.usahaq_skripsi.ui.login.Login
import com.example.usahaq_skripsi.ui.register.Register


class RegisterPage1(private val mRegisterActivityListener: RegisterActivityListener) : Fragment() {

    private lateinit var binding: FragmentRegisterPage1Binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterPage1Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRegisterActivityListener.fragment1(binding)

        binding.apply {
            if(etName.text.toString().trim().isEmpty()){
                Register.isEmptyFields = true
                etName.error = "This field can't be empty"
            }
            if(etEmail.text.toString().trim().isEmpty()){
                Register.isEmptyFields = true
                etEmail.error = "This field can't be empty"
            }
            if(etPassword.text.toString().trim().isEmpty()){
                Register.isEmptyFields = true
                etPassword.error = "This field can't be empty"
            }
            if(etName.text.toString().trim().isNotEmpty()
                && etEmail.text.toString().trim().isNotEmpty()
                && etPassword.text.toString().trim().isNotEmpty()
               ){
                Register.isEmptyFields = false
            }

            tvbtnLogin.setOnClickListener {
                startActivity(Intent(requireContext(), Login::class.java))
            }
        }
    }


}