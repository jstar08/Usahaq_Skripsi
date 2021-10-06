package com.example.usahaq_skripsi.ui.intro.fragmentAppIntro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.usahaq_skripsi.R
import com.example.usahaq_skripsi.databinding.FragmentEasyRegisterBinding

class EasyRegister : Fragment() {
    private lateinit var binding : FragmentEasyRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEasyRegisterBinding.inflate(layoutInflater)
        return binding.root
    }
}