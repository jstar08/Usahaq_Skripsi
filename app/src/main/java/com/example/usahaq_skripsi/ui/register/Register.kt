package com.example.usahaq_skripsi.ui.register

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.usahaq_skripsi.R
import com.example.usahaq_skripsi.databinding.ActivityRegisterBinding
import com.example.usahaq_skripsi.databinding.FragmentRegisterPage1Binding
import com.example.usahaq_skripsi.databinding.FragmentRegisterPage2Binding
import com.example.usahaq_skripsi.databinding.FragmentRegisterPage3Binding
import com.example.usahaq_skripsi.listener.RegisterActivityListener
import com.example.usahaq_skripsi.model.Account
import com.example.usahaq_skripsi.ui.dashboard.DashboardActivity
import com.example.usahaq_skripsi.ui.register.registerpage.RegisterPage1
import com.example.usahaq_skripsi.ui.register.registerpage.RegisterPage2
import com.example.usahaq_skripsi.ui.register.registerpage.RegisterPage3
import com.example.usahaq_skripsi.util.ViewModelFactory
import com.example.usahaq_skripsi.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity(), RegisterActivityListener  {

    private lateinit var binding : ActivityRegisterBinding
    private lateinit var adapter: RegisterAdapter
    private lateinit var mRegisterActivityListener: RegisterActivityListener
    private lateinit var fragment1Binding: FragmentRegisterPage1Binding
    private lateinit var fragment2Binding: FragmentRegisterPage2Binding
    private lateinit var fragment3binding: FragmentRegisterPage3Binding
    private var account : Account = Account()
    private lateinit var viewModel: AuthViewModel

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mRegisterActivityListener = this
        adapter = RegisterAdapter(supportFragmentManager)
        auth = FirebaseAuth.getInstance()
        database = Firebase.firestore

        adapter.list.add(RegisterPage1(mRegisterActivityListener))
        adapter.list.add(RegisterPage2(mRegisterActivityListener))
        adapter.list.add(RegisterPage3(mRegisterActivityListener))

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        binding.apply {
            viewpager.adapter = adapter
            tvNumber.text = "1"
            tvNextAccept.text = "Next"
            btnNext.setOnClickListener {
                when(viewpager.currentItem){
                    0-> {
                        if(!isRegister1Empty()){
                            viewpager.currentItem++
                        }
                    }
                    1->{
                        if(!isRegister2Empty()){
                            viewpager.currentItem++
                        }
                    }
                }
            }

            viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {
                    if (position == adapter.list.size - 1) {
                        btnNext.setOnClickListener {
                            if(fragment3binding.cbTos.isChecked) register()
                        }
                    }
                    when (viewpager.currentItem) {
                        0 -> {
                            tvNumber.text = "1"
                            tvNextAccept.text = "Next"
                            indicator1.setTextColor(Color.WHITE)
                            indicator2.setTextColor(
                                ContextCompat.getColor(
                                    this@Register,
                                    R.color.blue_button
                                )
                            )
                            indicator3.setTextColor(
                                ContextCompat.getColor(
                                    this@Register,
                                    R.color.blue_button
                                )
                            )
                        }
                        1 -> {
                            account.name = fragment1Binding.etName.text.toString()
                            account.password = fragment1Binding.etPassword.text.toString()
                            account.customEmail = fragment1Binding.etEmail.text.toString()
                            tvNumber.text = "2"
                            tvNextAccept.text = "Next"
                            indicator1.setTextColor(
                                ContextCompat.getColor(
                                    this@Register,
                                    R.color.blue_button
                                )
                            )
                            indicator2.setTextColor(Color.WHITE)
                            indicator3.setTextColor(
                                ContextCompat.getColor(
                                    this@Register,
                                    R.color.blue_button
                                )
                            )
                        }
                        2 -> {
                            account.phoneNumber =
                                fragment2Binding.etPhoneNumber.text.toString()
                            account.location = fragment2Binding.etLocation.text.toString()
                            account.imageUrl = imageUrl
                            tvNumber.text = "3"
                            tvNextAccept.text = "Finish"
                            indicator1.setTextColor(
                                ContextCompat.getColor(
                                    this@Register,
                                    R.color.blue_button
                                )
                            )
                            indicator2.setTextColor(
                                ContextCompat.getColor(
                                    this@Register,
                                    R.color.blue_button
                                )
                            )
                            indicator3.setTextColor(Color.WHITE)
                        }
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                }

            })
        }
    }

    private fun isRegister1Empty() : Boolean {
        fragment1Binding.apply {
            if(etName.text.toString().trim().isEmpty()){
                etName.error = "This field can't be empty"
                return true
            }
            if(etEmail.text.toString().trim().isEmpty()){
                etEmail.error = "This field can't be empty"
                return true
            }
            if(etPassword.text.toString().trim().isEmpty()){
                etPassword.error = "This field can't be empty"
                return true
            }
            else return false
        }
    }

    private fun isRegister2Empty() : Boolean {
        fragment2Binding.apply {
                if(etLocation.text.toString().trim().isEmpty()){
                    etLocation.error = "This field can't be empty"
                    return true
                }
                if(etPhoneNumber.text.toString().trim().isEmpty()){
                    etPhoneNumber.error = "This field can't be empty"
                    return true
                }
                else return false
        }
    }

    private fun register() {
        viewModel.createAccount(account)
        viewModel.signIn(account.customEmail.toString(), account.password.toString())
    }

    override fun fragment1(binding: FragmentRegisterPage1Binding) {
        fragment1Binding = binding
    }

    override fun fragment2(binding: FragmentRegisterPage2Binding) {
        fragment2Binding = binding
    }

    override fun fragment3(binding : FragmentRegisterPage3Binding){
        fragment3binding = binding
    }

    companion object {
        var imageUrl : String ?= null
    }
}

class RegisterAdapter(manager: FragmentManager) :
    FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    val list: MutableList<Fragment> = ArrayList()

    override fun getCount(): Int = list.size

    override fun getItem(position: Int): Fragment = list[position]
}