package com.example.usahaq_skripsi.listener

import com.example.usahaq_skripsi.databinding.FragmentRegisterPage1Binding
import com.example.usahaq_skripsi.databinding.FragmentRegisterPage2Binding
import com.example.usahaq_skripsi.databinding.FragmentRegisterPage3Binding
import java.io.File

interface RegisterActivityListener {
    fun fragment1(binding: FragmentRegisterPage1Binding)
    fun fragment2(binding: FragmentRegisterPage2Binding)
    fun fragment3(binding: FragmentRegisterPage3Binding)
    /*fun fragment2ImageChanged(file: File)*/
}