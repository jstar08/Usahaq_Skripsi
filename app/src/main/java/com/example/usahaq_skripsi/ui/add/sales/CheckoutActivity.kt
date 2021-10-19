package com.example.usahaq_skripsi.ui.add.sales

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.usahaq_skripsi.adapter.CheckoutAdapter
import com.example.usahaq_skripsi.databinding.ActivityCheckoutBinding
import com.example.usahaq_skripsi.model.Business
import com.example.usahaq_skripsi.model.ProductSales
import com.example.usahaq_skripsi.model.ProductSold
import com.example.usahaq_skripsi.model.Sales
import com.example.usahaq_skripsi.util.ViewModelFactory
import com.example.usahaq_skripsi.viewmodel.ProductViewModel
import com.example.usahaq_skripsi.viewmodel.SalesViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCheckoutBinding
    private lateinit var adapter : CheckoutAdapter
    private lateinit var simpleDate: SimpleDateFormat
    private lateinit var calendar : Calendar
    private lateinit var viewmodel : SalesViewModel
    private lateinit var productViewModel: ProductViewModel

    private var productSales = ArrayList<ProductSales>()
    private var business : Business = Business()
    private var productSold = ArrayList<ProductSold>()
    private var sales = Sales()
    private var salesId = ""

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = CheckoutAdapter()

        salesId = UUID.randomUUID().toString()

        initDate()

        val factory = ViewModelFactory.getInstance(this)

        viewmodel = ViewModelProvider(this, factory)[SalesViewModel::class.java]
        productViewModel = ViewModelProvider(this, factory)[ProductViewModel::class.java]

        productSales = intent.getParcelableArrayListExtra<ProductSales>(PRODUCT_SALES) as ArrayList<ProductSales>
        business = intent.getParcelableExtra<Business>(BUSINESS)!!

        initTotalPrice()
        convertInToProductSold()

        adapter.productData = productSales
        binding.apply {
            tvIdTransaction.text = salesId
            btnBack.setOnClickListener { onBackPressed() }

            rvProductSales.adapter = adapter
            rvProductSales.layoutManager = LinearLayoutManager(this@CheckoutActivity)

            btnConfirmation.setOnClickListener {
                addSales()
                val intent = Intent(this@CheckoutActivity, CompleteTransactionActivity::class.java)
                intent.putExtra(CompleteTransactionActivity.TOTALPRICE, binding.tvTotalPaymentAmount.text.toString())
                startActivity(intent)
                setResult(0)
                finish()
            }
        }

    }

    private fun initDate(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            simpleDate = SimpleDateFormat("dd-M-yyyy, h:mm a", Locale.getDefault())
        }
        calendar = Calendar.getInstance()
        binding.tvDate.text = simpleDate.format(calendar.time)
    }

    private fun initTotalPrice(){
        var price = 0
        var number = 0
        for(i in productSales){
            price+= i.amount?.toInt()!!.times(i.price?.toInt()!!)
            number+= i.amount!!.toInt()
        }
        binding.tvPriceAmount.text = "Rp.$price"
        binding.tvTaxAmount.text = "Rp.${price.div(10)}"
        price+=price.div(10)
        binding.tvTotalPaymentAmount.text = "Rp.$price"
        binding.tvButtonQuantity.text = "$number items"
        sales.totalPrice = price.toString()
    }

    private fun addSales(){
        sales.businessId = business.businessId
        sales.salesId = salesId
        sales.date = binding.tvDate.text.toString()
        sales.paymentMethod = binding.spinnerPaymentMethod.selectedItem.toString()
        viewmodel.addSales(sales, productSold)
        editStock()
    }

    private fun editStock(){
        productViewModel.showListProduct(business.businessId!!).observe(this, {result ->
            for (i in result){
                for (j in productSold){
                    if(i.name.equals(j.name)){
                        val totalStock = i.stocks?.toInt()?.minus(j?.amount?.toInt()!!)
                        i.stocks = totalStock.toString()
                        productViewModel.editProduct(i)
                    }
                }
            }
        })
    }

    private fun convertInToProductSold(){
        for(i in productSales){
            productSold.add(
                ProductSold(
                    name = i.name,
                    price = i.price,
                    amount = i.amount,
                    note = i.note,
                    salesId = salesId,
                    productSoldId = UUID.randomUUID().toString()
                )
            )
        }
    }

    companion object{
        const val PRODUCT_SALES = "product_sales"
        const val BUSINESS = "business"
    }
}