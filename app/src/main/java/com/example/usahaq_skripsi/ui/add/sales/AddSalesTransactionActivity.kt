package com.example.usahaq_skripsi.ui.add.sales

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.usahaq_skripsi.R
import com.example.usahaq_skripsi.adapter.SalesProductAdapter
import com.example.usahaq_skripsi.databinding.ActivityAddSalesTransactionBinding
import com.example.usahaq_skripsi.databinding.SheetProductSalesBinding
import com.example.usahaq_skripsi.model.Business
import com.example.usahaq_skripsi.model.ProductSales
import com.example.usahaq_skripsi.util.ViewModelFactory
import com.example.usahaq_skripsi.util.productInToProductSales
import com.example.usahaq_skripsi.viewmodel.ProductViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class AddSalesTransactionActivity : AppCompatActivity(), SalesProductAdapter.OnClickListener {

    private lateinit var binding : ActivityAddSalesTransactionBinding
    private lateinit var productViewModel: ProductViewModel
    private lateinit var adapter : SalesProductAdapter
    private lateinit var sheetBehaviour: BottomSheetBehavior<View>
    private lateinit var sheet: SheetProductSalesBinding
    private lateinit var sheetDialog: BottomSheetDialog

    private var business : Business = Business()
    private var productSales = ArrayList<ProductSales>()
    private var totalAmount = 0

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==0){
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        showProduct(adapter)
        totalAmount = 0
        productSales.clear()
        binding.btnConfirmation.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSalesTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = SalesProductAdapter(this)

        business = intent.getParcelableExtra<Business>(BUSINESS_DATA)!!

        sheet = SheetProductSalesBinding.inflate(layoutInflater)
        sheetBehaviour = BottomSheetBehavior.from(binding.bottomSheet)
        sheetDialog = BottomSheetDialog(this)
        sheetDialog.setContentView(sheet.root)

        val factory = ViewModelFactory.getInstance(this)
        productViewModel = ViewModelProvider(this, factory)[ProductViewModel::class.java]

        binding.apply {
            btnBack.setOnClickListener {
                onBackPressed()
            }

            btnConfirmation.visibility = View.GONE
            btnConfirmation.setOnClickListener {
                val intent = Intent(this@AddSalesTransactionActivity, CheckoutActivity::class.java)
                intent.putParcelableArrayListExtra(CheckoutActivity.PRODUCT_SALES, productSales)
                intent.putExtra(CheckoutActivity.BUSINESS, business)
                startActivity(intent)
            }


        }
    }

    private fun showProduct(adapter: SalesProductAdapter){
        productViewModel.showListProduct(business.businessId!!).observe(this, { result ->
            adapter.productData.clear()
            val productSales = productInToProductSales(result)
            adapter.productData.addAll(productSales)
            binding.rvProduct.visibility = View.VISIBLE
            binding.rvProduct.adapter = adapter
        })

    }

    private fun showBottomSheet(productData: ProductSales, index: Int){
        if (sheetBehaviour.state == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        sheet.btnClose.setOnClickListener {
            sheetDialog.dismiss()
        }
        sheetDialog.window?.statusBarColor = 0x04000000
        sheetDialog.show()

        var number = 0
        var productListed = false

        if(productData.amount=="0"||productData.amount==null){
            number = 1
            totalAmount++
        }
        else{
            number = productData.amount!!.toInt()
        }

        sheet.apply {
            tvRion.text = productData.name
            priceAmount.text = "Rp.${productData.price}"
            stockAmount.text = "${productData.stocks} pcs"
            tvPrice.text = "Rp.${productData.price}"
            tvButtonQuantity.text = "$number items"
            Glide.with(this@AddSalesTransactionActivity)
                .load(productData.imageUrl)
                .into(ivBackCoverDetail)
            tvQuantityAmount.text = number.toString()

            btnPlus.setOnClickListener {
                number++
                totalAmount++
                tvQuantityAmount.text = number.toString()
                var totalPrice = number*productData.price?.toInt()!!
                tvPrice.text = "Rp.$totalPrice"
                sheet.tvAdditems.text = "New Transaction"
                sheet.tvButtonQuantity.text = "$number items"
                sheet.ivBtnColor.setColorFilter(
                    ContextCompat.getColor(
                        this@AddSalesTransactionActivity,
                        R.color.light_blue
                    )
                )
            }

            btnMinus.setOnClickListener {
                if(number>0){
                    number--
                    totalAmount--
                    tvQuantityAmount.text = number.toString()
                    var totalPrice = number*productData.price?.toInt()!!
                    tvPrice.text = "Rp.$totalPrice"
                    sheet.tvButtonQuantity.text = "$number items"
                }
                if (number == 0) {
                    sheet.tvAdditems.text = getString(R.string.remove)
                    sheet.tvButtonQuantity.text = getString(R.string.all_items)
                    sheet.ivBtnColor.setColorFilter(
                        ContextCompat.getColor(
                            this@AddSalesTransactionActivity,
                            R.color.red
                        )
                    )
                }
            }

            btnConfirmation.setOnClickListener {
                sheetDialog.dismiss()
                adapter.productData[index].amount = number.toString()
                adapter.productData[index].note = sheet.etNotes.text.toString()
                if(productSales.isNotEmpty()){
                    for(i in productSales){
                        if (i.name.equals(productData.name)){
                            i.amount = number.toString()
                            productListed = true
                        }
                    }
                    if (!productListed){
                        productSales.add(adapter.productData[index])
                    }
                }
                else{
                    productSales.add(adapter.productData[index])
                }
                binding.rvProduct.adapter = adapter
                showConfirmation()
            }
        }
    }

    private fun showConfirmation(){
        if (totalAmount>0){
            binding.apply {
                btnConfirmation.visibility = View.VISIBLE
                tvButtonQuantity.text = "$totalAmount items"
            }
        }
        else{
            binding.btnConfirmation.visibility = View.GONE
        }
    }

    companion object{
        const val BUSINESS_DATA = "business_data"
    }

    override fun onClick(productData: ProductSales, index : Int) {
        showBottomSheet(productData, index)
    }
}