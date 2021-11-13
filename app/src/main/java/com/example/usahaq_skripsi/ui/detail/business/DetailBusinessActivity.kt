package com.example.usahaq_skripsi.ui.detail.business

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import com.example.usahaq_skripsi.R
import com.example.usahaq_skripsi.databinding.ActivityDetailBusinessBinding
import com.example.usahaq_skripsi.model.Business
import com.example.usahaq_skripsi.ui.add.product.AddProductActivity
import com.example.usahaq_skripsi.ui.add.purchase.AddPurchaseActivity
import com.example.usahaq_skripsi.ui.add.sales.AddSalesTransactionActivity
import com.example.usahaq_skripsi.ui.detail.financial_report.FinancialReportActivity
import com.example.usahaq_skripsi.ui.edit.EditBusinessActivity
import com.example.usahaq_skripsi.util.ViewModelFactory
import com.example.usahaq_skripsi.viewmodel.BusinessViewModel
import com.example.usahaq_skripsi.viewmodel.PurchaseViewModel
import com.example.usahaq_skripsi.viewmodel.SalesViewModel
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.google.android.material.tabs.TabLayoutMediator
import android.widget.LinearLayout

import android.widget.TextView

class DetailBusinessActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailBusinessBinding
    private var businessData : Business?= null
    private var businessId : String?=null

    private lateinit var viewModel: BusinessViewModel
    private lateinit var purchaseViewModel : PurchaseViewModel
    private lateinit var salesViewModel : SalesViewModel

    private var totalBuy = 0
    private var totalSales = 0

    lateinit var prefrence : SharedPreferences
    val pref_show_intro = "Intro"

    override fun onResume() {
        super.onResume()
        loadBusinessData(businessId!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBusinessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        businessId = intent.getStringExtra(BUSINESS_ID)

        prefrence = getSharedPreferences("tutorial", Context.MODE_PRIVATE)

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

            btnInfo.setOnClickListener {
                tutorial()
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

        if(prefrence.getBoolean(pref_show_intro, true)){
            tutorial()
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



    private fun tutorial(){
        binding.apply {
            TapTargetSequence(this@DetailBusinessActivity)
                .targets(
                    TapTarget.forView(btnAddProduct,
                        getString(R.string.floating_action_menu),
                        getString(R.string.fam_desc))
                        .outerCircleColor(R.color.light_blue)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(20)
                        .titleTextColor(R.color.white)
                        .descriptionTextSize(15)
                        .descriptionTextColor(R.color.white)
                        .textColor(R.color.white)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .targetRadius(45),
                    TapTarget.forView(cvClipboard,
                        getString(R.string.financial_report),
                        getString(R.string.financial_desc))
                        .outerCircleColor(R.color.light_blue)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(20)
                        .titleTextColor(R.color.white)
                        .descriptionTextSize(15)
                        .descriptionTextColor(R.color.white)
                        .textColor(R.color.white)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .targetRadius(120),
                    TapTarget.forView(
                        ((tableLayoutDetail.getChildAt(0) as LinearLayout).getChildAt(0)
                                as LinearLayout).getChildAt(1) as TextView,
                        getString(R.string.product),
                        getString(R.string.productDesc))
                        .outerCircleColor(R.color.light_blue)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(20)
                        .titleTextColor(R.color.white)
                        .descriptionTextSize(15)
                        .descriptionTextColor(R.color.white)
                        .textColor(R.color.white)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .targetRadius(60),
                    TapTarget.forView(
                        ((tableLayoutDetail.getChildAt(0) as LinearLayout).getChildAt(1)
                                as LinearLayout).getChildAt(1) as TextView,
                        getString(R.string.purchase),
                        getString(R.string.purchaseDesc))
                        .outerCircleColor(R.color.light_blue)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(20)
                        .titleTextColor(R.color.white)
                        .descriptionTextSize(15)
                        .descriptionTextColor(R.color.white)
                        .textColor(R.color.white)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .targetRadius(60),
                    TapTarget.forView(
                        ((tableLayoutDetail.getChildAt(0) as LinearLayout).getChildAt(2)
                                as LinearLayout).getChildAt(1) as TextView,
                        getString(R.string.sales),
                        getString(R.string.sellDesc))
                        .outerCircleColor(R.color.light_blue)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(20)
                        .titleTextColor(R.color.white)
                        .descriptionTextSize(15)
                        .descriptionTextColor(R.color.white)
                        .textColor(R.color.white)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .targetRadius(60),
                    TapTarget.forView(btnInfo,
                        getString(R.string.info_button),
                        getString(R.string.info_desc))
                        .outerCircleColor(R.color.light_blue)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(20)
                        .titleTextColor(R.color.white)
                        .descriptionTextSize(15)
                        .descriptionTextColor(R.color.white)
                        .textColor(R.color.white)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .targetRadius(30)
                ).start()
        }
        val editor = prefrence.edit()
        editor.putBoolean(pref_show_intro, false)
        editor.apply()
    }

    companion object{
        const val BUSINESS_ID = "business_id"
    }
}