package com.example.usahaq_skripsi.ui.detail.financial_report

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.ViewModelProvider
import com.example.usahaq_skripsi.databinding.ActivityFinancialReportBinding
import com.example.usahaq_skripsi.model.Purchase
import com.example.usahaq_skripsi.model.Sales
import com.example.usahaq_skripsi.util.ViewModelFactory
import com.example.usahaq_skripsi.viewmodel.PurchaseViewModel
import com.example.usahaq_skripsi.viewmodel.SalesViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FinancialReportActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding : ActivityFinancialReportBinding

    private lateinit var purchaseViewModel: PurchaseViewModel
    private lateinit var salesViewModel: SalesViewModel

    private lateinit var simpleDate: SimpleDateFormat

    private var date = ArrayList<Date>()
    private var purchase = ArrayList<Purchase>()
    private var sales = ArrayList<Sales>()

    private var businessId = ""
    private var totalOutcome = 0
    private var totalIncome = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinancialReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        businessId = intent.getStringExtra(BUSINESS_ID)!!

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            simpleDate = SimpleDateFormat("d-M-yyyy", Locale.getDefault())
        }

        val factory = ViewModelFactory.getInstance(this)
        purchaseViewModel = ViewModelProvider(this, factory)[PurchaseViewModel::class.java]
        salesViewModel = ViewModelProvider(this, factory)[SalesViewModel::class.java]

        binding.apply {
            btnBack.setOnClickListener { onBackPressed() }

            spinnerOutlet.onItemSelectedListener = this@FinancialReportActivity
        }
    }

    private fun getDaysAgo(daysAgo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)

        return calendar.time
    }

    private fun showData(){
        totalOutcome = 0
        totalIncome = 0
        for (i in purchase){
            totalOutcome+= i.price?.toInt()!!
        }
        for (i in sales){
            totalIncome+= i.totalPrice?.toInt()!!
        }
        binding.apply {
            tvOutcomeAmount.text = "Rp.$totalOutcome"
            tvIncomeAmount.text = "Rp.$totalIncome"
            tvPrice.text = "Rp.$totalOutcome"
            netPrice.text = "Rp.$totalIncome"
            var grossProfit = totalIncome - totalOutcome
            grossProfitPrice.text = "Rp.$grossProfit"
            var totalTransation = purchase.size+sales.size
            transactionPrice.text = totalTransation.toString()
            var average = totalIncome.div(sales.size)
            averagePrice.text = "Rp.$average"
        }
    }

    private fun getData(){
        date.clear()
        purchase.clear()
        sales.clear()
        val spinnerValue = binding.spinnerOutlet.selectedItem.toString()

        if(spinnerValue == "Last 7 days" || spinnerValue == "7 hari terakhir"){
            for (i in 0..7){
                date.add(getDaysAgo(i))
            }
            getPurchaseSalesData()
        }
        else{
            for (i in 0..30){
                date.add(getDaysAgo(i))
            }
            getPurchaseSalesData()
        }


    }

    private fun getPurchaseSalesData(){
        purchaseViewModel.showListPurchase(businessId).observe(this , {result ->
            for (i in result){
                for (j in date){
                    if (i.date.equals(simpleDate.format(j))){
                        purchase.add(i)
                    }
                }
            }
            salesViewModel.showListSales(businessId).observe(this, {result ->
                for (i in result){
                    for (j in date){
                        var newDate = i.date.toString().substringBefore(',')
                        Log.d("date", newDate)
                        if(newDate == simpleDate.format(j)){
                            sales.add(i)
                        }
                    }
                }
                showData()
            })
        })
    }

    companion object{
        const val BUSINESS_ID = "business_id"
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        getData()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}