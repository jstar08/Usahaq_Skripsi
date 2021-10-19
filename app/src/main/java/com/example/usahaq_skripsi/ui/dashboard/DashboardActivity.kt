package com.example.usahaq_skripsi.ui.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.usahaq_skripsi.adapter.BusinessAdapter
import com.example.usahaq_skripsi.databinding.ActivityDashboardBinding
import com.example.usahaq_skripsi.ui.add.business.AddBusinessActivity
import com.example.usahaq_skripsi.ui.setting.SettingActivity
import com.example.usahaq_skripsi.util.ViewModelFactory
import com.example.usahaq_skripsi.viewmodel.AuthViewModel
import com.example.usahaq_skripsi.viewmodel.BusinessViewModel
import com.facebook.shimmer.ShimmerFrameLayout

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDashboardBinding
    private lateinit var adapter : BusinessAdapter
    private lateinit var businessViewModel : BusinessViewModel
    private lateinit var accountViewModel : AuthViewModel
    private lateinit var shimmerFrameLayout : ShimmerFrameLayout
    private lateinit var shimmerProfile : ShimmerFrameLayout

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

        loadShimmer()

        showAccountData()

        binding.apply {
            btnSettings.setOnClickListener {
                startActivity(Intent(this@DashboardActivity, SettingActivity::class.java))
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

    private fun loadShimmer(){
        shimmerFrameLayout = binding.shimmerLayout
        shimmerProfile = binding.shimmerProfile
        shimmerFrameLayout.visibility = View.VISIBLE
        binding.rvBusiness.visibility = View.GONE
        shimmerProfile.visibility = View.VISIBLE
        binding.clProfileData.visibility = View.GONE
    }

    private fun showBusiness(adapter: BusinessAdapter){
       businessViewModel.showlistBusiness(adapter).observe(this, { result ->
           shimmerFrameLayout.visibility = View.GONE
           if(result!=null){
               adapter.businessData.clear()
               adapter.businessData.addAll(result)
               binding.clCreateBusiness.visibility = View.GONE
               binding.rvBusiness.visibility = View.VISIBLE
               binding.rvBusiness.adapter = adapter
           }
           else{
            binding.clCreateBusiness.visibility = View.VISIBLE
           }
       })

    }

    private fun showAccountData(){
        accountViewModel.getAccountData().observe(this, {
            binding.apply {
                tvAccountEmail.text = it.customEmail
                tvAccountName.text = it.name
                tvAccountLocation.text = it.location
                tvPhoneNumber.text = it.phoneNumber
                Glide.with(this@DashboardActivity).load(it.imageUrl).into(binding.ivAccountImage)
                shimmerProfile.visibility = View.GONE
                binding.clProfileData.visibility = View.VISIBLE
            }
        })
    }
}