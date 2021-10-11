package com.example.usahaq_skripsi.ui.detail.business

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.usahaq_skripsi.databinding.ActivityDetailBusinessBinding
import com.example.usahaq_skripsi.model.Business
import com.example.usahaq_skripsi.ui.add.AddProductActivity
import com.example.usahaq_skripsi.ui.edit.EditBusinessActivity
import com.example.usahaq_skripsi.ui.edit.EditProductActivity
import com.example.usahaq_skripsi.util.ViewModelFactory
import com.example.usahaq_skripsi.viewmodel.BusinessViewModel
import com.google.android.material.tabs.TabLayoutMediator

class DetailBusinessActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailBusinessBinding
    private var businessData : Business?= null
    private var businessId : String?=null
    private lateinit var viewModel: BusinessViewModel

    override fun onResume() {
        super.onResume()
        loadBusinessData(businessId!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBusinessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        businessId = intent.getStringExtra(BUSINESS_ID)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[BusinessViewModel::class.java]

        val adapter = ViewPagerAdapter(this, businessId!!)

        binding.apply {



            cvClipboard.setOnClickListener {

            }

            btnAddProduct.setOnClickListener{
                val intent = Intent(this@DetailBusinessActivity, AddProductActivity::class.java)
                intent.putExtra(AddProductActivity.BUSINESS_DATA, businessData)
                startActivity(intent)
            }

            btnAddTransaction.setOnClickListener {

            }

            btnBack.setOnClickListener {
                onBackPressed()
            }

            btnEdit.setOnClickListener {
                val intent = Intent(this@DetailBusinessActivity, EditBusinessActivity::class.java)
                intent.putExtra(EditBusinessActivity.BUSINESS, businessData)
                startActivity(intent)
            }


            viewpagerDetail.adapter = adapter
            viewpagerDetail.offscreenPageLimit = adapter.itemCount
            TabLayoutMediator(
                binding.tableLayoutDetail,
                binding.viewpagerDetail
            ) { tab, position ->
                when (position) {
                    0 -> tab.text = "Product"
                    1 -> tab.text = "Purchase"
                    2 -> tab.text = "Sales"
                }
            }.attach()
        }
    }

    private fun loadBusinessData(businessId : String){
        viewModel.detaiBusiness(businessId).observe(this, {result ->
            businessData = result
            binding.tvBusinessNameDetail.text = businessData?.name
        })
    }

    companion object{
        const val BUSINESS_ID = "business_id"
    }
}