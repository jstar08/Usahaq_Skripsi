package com.example.usahaq_skripsi.ui.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.usahaq_skripsi.adapter.BusinessAdapter
import com.example.usahaq_skripsi.databinding.ActivityDashboardBinding
import com.example.usahaq_skripsi.util.ViewModelFactory
import com.example.usahaq_skripsi.viewmodel.AuthViewModel
import com.example.usahaq_skripsi.viewmodel.BusinessViewModel

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDashboardBinding
    private lateinit var adapter : BusinessAdapter
    private lateinit var businessViewModel : BusinessViewModel
    private lateinit var accountViewModel : AuthViewModel

    override fun onResume() {
        super.onResume()

        showBusiness(adapter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = BusinessAdapter()
        val factory = ViewModelFactory.getInstance(this)
        businessViewModel = ViewModelProvider(this, factory)[BusinessViewModel::class.java]
        accountViewModel = ViewModelProvider(this,factory)[AuthViewModel::class.java]

        showAccountData()

        binding.apply {
            btnLogout.setOnClickListener {
                startActivity(Intent(this@DashboardActivity, AddBusinessActivity::class.java))
            }

            fabAddBusiness.setOnClickListener {
                startActivity(Intent(this@DashboardActivity, AddBusinessActivity::class.java))
            }

            rvBusiness.layoutManager = LinearLayoutManager(this@DashboardActivity)

            btnCreateBusiness.setOnClickListener {
                startActivity(Intent(this@DashboardActivity, AddBusinessActivity::class.java))
            }
        }
    }

    private fun showBusiness(adapter: BusinessAdapter){
       businessViewModel.showlistBusiness(adapter).observe(this, { result ->
           adapter.businessData.clear()
           adapter.businessData.addAll(result)
           binding.rvBusiness.adapter = adapter
       })

    }

    private fun showAccountData(){
        accountViewModel.getAccountData().observe(this, {
            binding.apply {
                tvAccountEmail.text = it.customEmail
                tvAccountName.text = it.name
                tvAccountLocation.text = "${it.address}, ${it.city}"
                tvPostalCode.text = it.postalCode
                Glide.with(this@DashboardActivity).load(it.imageUrl).into(binding.ivAccountImage)
            }
        })
    }

    companion object{
        var isCompleted = false
    }
}