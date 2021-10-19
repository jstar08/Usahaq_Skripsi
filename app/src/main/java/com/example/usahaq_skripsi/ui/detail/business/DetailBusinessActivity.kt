package com.example.usahaq_skripsi.ui.detail.business

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.usahaq_skripsi.R
import com.example.usahaq_skripsi.databinding.ActivityDetailBusinessBinding
import com.example.usahaq_skripsi.model.Business
import com.example.usahaq_skripsi.model.Purchase
import com.example.usahaq_skripsi.model.Sales
import com.example.usahaq_skripsi.ui.add.product.AddProductActivity
import com.example.usahaq_skripsi.ui.add.purchase.AddPurchaseActivity
import com.example.usahaq_skripsi.ui.add.sales.AddSalesTransactionActivity
import com.example.usahaq_skripsi.ui.detail.financial_report.FinancialReportActivity
import com.example.usahaq_skripsi.ui.edit.EditBusinessActivity
import com.example.usahaq_skripsi.util.ViewModelFactory
import com.example.usahaq_skripsi.viewmodel.BusinessViewModel
import com.example.usahaq_skripsi.viewmodel.PurchaseViewModel
import com.example.usahaq_skripsi.viewmodel.SalesViewModel
import com.google.android.material.tabs.TabLayoutMediator

class DetailBusinessActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailBusinessBinding
    private var businessData : Business?= null
    private var businessId : String?=null

    private lateinit var viewModel: BusinessViewModel
    private lateinit var purchaseViewModel : PurchaseViewModel
    private lateinit var salesViewModel : SalesViewModel

    private var totalBuy = 0
    private var totalSales = 0

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
        purchaseViewModel = ViewModelProvider(this, factory)[PurchaseViewModel::class.java]
        salesViewModel = ViewModelProvider(this, factory)[SalesViewModel::class.java]

        val adapter = ViewPagerAdapter(this, businessId!!)

        loadIncomeOutcome()

        binding.apply {

            cvClipboard.setOnClickListener {
                val intent = Intent(this@DetailBusinessActivity, FinancialReportActivity::class.java)
                intent.putExtra(FinancialReportActivity.BUSINESS_ID, businessId)
                startActivity(intent)
            }

            btnAddProduct.setOnClickListener{
                val intent = Intent(this@DetailBusinessActivity, AddProductActivity::class.java)
                intent.putExtra(AddProductActivity.BUSINESS_DATA, businessData)
                startActivity(intent)
            }

            btnAddTransaction.setOnClickListener {
                val intent = Intent(this@DetailBusinessActivity, AddSalesTransactionActivity::class.java)
                intent.putExtra(AddSalesTransactionActivity.BUSINESS_DATA, businessData)
                startActivity(intent)
            }

            btnAddPurchase.setOnClickListener {
                val intent = Intent(this@DetailBusinessActivity, AddPurchaseActivity::class.java)
                intent.putExtra(AddPurchaseActivity.BUSINESS, businessData)
                startActivity(intent)
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
                    0 -> tab.text = getString(R.string.product)
                    1 -> tab.text = getString(R.string.purchase)
                    2 -> tab.text = getString(R.string.sales)
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

    private fun loadIncomeOutcome(){
        purchaseViewModel.showListPurchase(businessId!!).observe(this , {result ->
            for(i in result){
                totalBuy+= i.price?.toInt()!!
            }
            binding.tvOutcomeAmount.text = "Rp.$totalBuy"
        })

        salesViewModel.showListSales(businessId!!).observe(this, {result ->
            for (i in result){
                totalSales+= i.totalPrice?.toInt()!!
            }
            binding.tvIncomeAmount.text = "Rp.$totalSales"
        })
    }

    companion object{
        const val BUSINESS_ID = "business_id"
    }
}