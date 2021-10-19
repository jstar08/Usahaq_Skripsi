package com.example.usahaq_skripsi.ui.detail.product

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.usahaq_skripsi.R
import com.example.usahaq_skripsi.databinding.ActivityDetailProductBinding
import com.example.usahaq_skripsi.databinding.SheetDeleteProductBinding
import com.example.usahaq_skripsi.model.Product
import com.example.usahaq_skripsi.ui.edit.EditProductActivity
import com.example.usahaq_skripsi.util.ViewModelFactory
import com.example.usahaq_skripsi.viewmodel.ProductViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class DetailProductActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailProductBinding
    private lateinit var sheetBehaviour: BottomSheetBehavior<View>
    private lateinit var sheet: SheetDeleteProductBinding
    private lateinit var sheetDialog: BottomSheetDialog
    private var productId : String?=null
    private var productData : Product?=null
    private lateinit var viewModel: ProductViewModel

    override fun onResume() {
        super.onResume()
        loadData(productId!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productId = intent.getStringExtra(PRODUCT_ID)

        sheet = SheetDeleteProductBinding.inflate(layoutInflater)
        sheetBehaviour = BottomSheetBehavior.from(binding.bottomSheet)
        sheetDialog = BottomSheetDialog(this)
        sheetDialog.setContentView(sheet.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[ProductViewModel::class.java]

        binding.apply {
            btnBack.setOnClickListener {
                onBackPressed()
            }

            btnEdit.setOnClickListener {
                val intent = Intent(this@DetailProductActivity, EditProductActivity::class.java)
                intent.putExtra(EditProductActivity.PRODUCT, productData)
                startActivity(intent)
            }

            btnDelete.setOnClickListener {
                deleteProduct()
            }
        }
    }

    private fun loadData(productId : String) {
        viewModel.detailProduct(productId).observe(this, {result ->
            productData = result
            loadTextData(productData!!)
        })
    }

    private fun loadTextData(productData: Product){
        binding.apply {
            Glide.with(this@DetailProductActivity).load(productData.imageUrl)
                .into(profileProduct)
            tvProductName.text = productData.name
            tvPriceAmount.text = "Rp ${productData.price}"
            tvStocksAmount.text = "${productData.stocks} pcs"
            tvDesc.text = productData.description
        }
    }

    private fun deleteProduct(){
        if (sheetBehaviour.state == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        sheet.btnClose.setOnClickListener {
            sheetDialog.dismiss()
        }
        sheetDialog.window?.statusBarColor = 0x04000000
        sheetDialog.show()

        sheet.tvConfirm.text = getString(R.string.delete_confirmation, getString(R.string.product))
        sheet.tvDeleteProduct.text = getString(R.string.delete_product, getString(R.string.product))

        sheet.btnConfirmation.setOnClickListener {
            viewModel.deleteProduct(productId!!)
            finish()
        }
    }

    companion object{
        const val PRODUCT_ID = "PRODUCT_ID"
    }
}