package com.example.usahaq_skripsi.ui.detail.sales

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.usahaq_skripsi.adapter.SalesDetailAdapter
import com.example.usahaq_skripsi.databinding.ActivityDetailSalesBinding
import com.example.usahaq_skripsi.databinding.SheetDeleteProductBinding
import com.example.usahaq_skripsi.model.ProductSold
import com.example.usahaq_skripsi.model.Sales
import com.example.usahaq_skripsi.util.ViewModelFactory
import com.example.usahaq_skripsi.viewmodel.ProductViewModel
import com.example.usahaq_skripsi.viewmodel.SalesViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class DetailSalesActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailSalesBinding
    private lateinit var sheetBehaviour: BottomSheetBehavior<View>
    private lateinit var sheet: SheetDeleteProductBinding
    private lateinit var sheetDialog: BottomSheetDialog
    private var salesId : String?=null
    private var salesData : Sales?=null
    private lateinit var viewModel: SalesViewModel
    private lateinit var productViewModel: ProductViewModel
    private lateinit var adapter: SalesDetailAdapter
    private var productSold = ArrayList<ProductSold>()
    private lateinit var shimmerFrameLayout : ShimmerFrameLayout

    override fun onResume() {
        super.onResume()
        loadData(salesId!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSalesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        salesId = intent.getStringExtra(SALES_ID)

        adapter = SalesDetailAdapter()

        shimmerFrameLayout = binding.shimmerLayout
        binding.shimmerLayout.visibility = View.VISIBLE

        sheet = SheetDeleteProductBinding.inflate(layoutInflater)
        sheetBehaviour = BottomSheetBehavior.from(binding.bottomSheet)
        sheetDialog = BottomSheetDialog(this)
        sheetDialog.setContentView(sheet.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[SalesViewModel::class.java]
        productViewModel = ViewModelProvider(this, factory)[ProductViewModel::class.java]

        binding.apply {
            btnBack.setOnClickListener { onBackPressed() }

            btnDelete.setOnClickListener { openDeleteBottomSheet() }
        }
    }

    private fun initTotalPrice(){
        var price = 0
        var number = 0
        for (i in productSold){
            price+= i.amount?.toInt()!!.times(i.price?.toInt()!!)
            number+= i.amount!!.toInt()
        }
        binding.tvPriceAmount.text = "Rp.$price"
        binding.tvTaxAmount.text = "Rp.${price.div(10)}"
    }

    private fun loadData(salesId : String) {
        viewModel.detailSales(salesId).observe(this, { result ->
            salesData = result
            loadTextData(salesData!!)
        })
    }

    private fun loadTextData(salesData: Sales){
        binding.apply {
            tvIdTransaction.text = salesData.salesId
            tvPaymentMethodValue.text = salesData.paymentMethod
            tvDate.text = salesData.date
            viewModel.detailProductSold(salesId!!).observe(this@DetailSalesActivity, {result ->
                adapter.productData.addAll(result)
                binding.rvProductSales.adapter = adapter
                productSold.addAll(result)
                rvProductSales.layoutManager = LinearLayoutManager(this@DetailSalesActivity)
                initTotalPrice()
                tvTotalPaymentAmount.text = salesData.totalPrice
                binding.shimmerLayout.visibility = View.GONE
            })
        }
    }

    private fun openDeleteBottomSheet(){
        if (sheetBehaviour.state == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        sheet.btnClose.setOnClickListener {
            sheetDialog.dismiss()
        }
        sheetDialog.window?.statusBarColor = 0x04000000
        sheetDialog.show()

        sheet.btnConfirmation.setOnClickListener {
            deleteSales()
            finish()
        }
    }

    private fun deleteSales(){
        productViewModel.showListProduct(salesData?.businessId!!).observe(this, {result ->
            for (i in result){
                for (j in productSold){
                    if(i.name.equals(j.name)){
                        val totalStock = i.stocks?.toInt()?.plus(j?.amount?.toInt()!!)
                        i.stocks = totalStock.toString()
                        productViewModel.editProduct(i)
                    }
                }
            }
            viewModel.deleteSales(salesId!!)
        })
    }

    companion object{
        const val SALES_ID = "sales_id"
    }
}