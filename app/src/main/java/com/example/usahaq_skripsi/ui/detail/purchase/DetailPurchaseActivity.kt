package com.example.usahaq_skripsi.ui.detail.purchase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.usahaq_skripsi.databinding.ActivityDetailPurchaseBinding
import com.example.usahaq_skripsi.databinding.SheetDeleteProductBinding
import com.example.usahaq_skripsi.model.Product
import com.example.usahaq_skripsi.model.Purchase
import com.example.usahaq_skripsi.ui.edit.EditProductActivity
import com.example.usahaq_skripsi.ui.edit.EditPurchaseActivity
import com.example.usahaq_skripsi.util.ViewModelFactory
import com.example.usahaq_skripsi.viewmodel.ProductViewModel
import com.example.usahaq_skripsi.viewmodel.PurchaseViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class DetailPurchaseActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailPurchaseBinding
    private lateinit var sheetBehaviour: BottomSheetBehavior<View>
    private lateinit var sheet: SheetDeleteProductBinding
    private lateinit var sheetDialog: BottomSheetDialog
    private var purchaseId : String?=null
    private var purchaseData : Purchase?=null
    private lateinit var viewModel: PurchaseViewModel
    private lateinit var productViewModel : ProductViewModel
    private var productExisted : Boolean = false

    private var product : Product = Product()


    override fun onResume() {
        super.onResume()
        loadData(purchaseId!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        purchaseId = intent.getStringExtra(PURCHASE_ID)

        sheet = SheetDeleteProductBinding.inflate(layoutInflater)
        sheetBehaviour = BottomSheetBehavior.from(binding.bottomSheet)
        sheetDialog = BottomSheetDialog(this)
        sheetDialog.setContentView(sheet.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[PurchaseViewModel::class.java]
        productViewModel = ViewModelProvider(this, factory)[ProductViewModel::class.java]

        binding.apply {
            btnBack.setOnClickListener {
                onBackPressed()
            }

            btnEdit.setOnClickListener {
                val intent = Intent(this@DetailPurchaseActivity, EditPurchaseActivity::class.java)
                intent.putExtra(EditPurchaseActivity.PURCHASE, purchaseData)
                startActivity(intent)
            }

            btnDelete.setOnClickListener {
                deletePurchase()
            }
        }
    }

    private fun loadData(purchaseId : String) {
        viewModel.detailPurchase(purchaseId).observe(this, {result ->
            purchaseData = result
            loadTextData(purchaseData!!)
        })
    }

    private fun loadTextData(purchaseData: Purchase){
        binding.apply {
            Glide.with(this@DetailPurchaseActivity).load(purchaseData.imageUrl)
                .into(profileProduct)
            tvProductName.text = purchaseData.name
            tvPriceAmount.text = "Rp ${purchaseData.price}"
            tvStocksAmount.text = "${purchaseData.stocks} pcs"
            tvDateValue.text = purchaseData.date
        }
    }

    private fun deletePurchase(){
        if (sheetBehaviour.state == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        sheet.btnClose.setOnClickListener {
            sheetDialog.dismiss()
        }
        sheetDialog.window?.statusBarColor = 0x04000000
        sheetDialog.show()

        sheet.btnConfirmation.setOnClickListener {
            viewModel.deletePurchase(purchaseId!!)
            updateProductStocks()
            finish()
        }
    }

    private fun updateProductStocks(){
        productViewModel.showListProduct(purchaseData?.businessId!!).observe(this, {result->
            for(i in result){
                if(purchaseData?.name.equals(i.name, ignoreCase = true)){
                    productExisted = true
                    product = i
                }
            }
            if(productExisted){
                val totalStock = product.stocks?.toInt()?.minus(purchaseData?.stocks?.toInt()!!)
                product.stocks = totalStock.toString()
                productViewModel.editProduct(product)
            }
        })
    }

    companion object{
        const val PURCHASE_ID = "purchase_id"
    }
}