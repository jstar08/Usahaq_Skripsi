package com.example.usahaq_skripsi.ui.register.registerpage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.usahaq_skripsi.databinding.FragmentRegisterPage3Binding
import com.example.usahaq_skripsi.listener.RegisterActivityListener
import com.example.usahaq_skripsi.ui.register.Register

class RegisterPage3(private val mRegisterActivityListener: RegisterActivityListener) : Fragment() {
    private lateinit var binding: FragmentRegisterPage3Binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterPage3Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRegisterActivityListener.fragment3(binding)
    }
}