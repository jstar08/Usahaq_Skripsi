package com.example.usahaq_skripsi.ui.edit

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.usahaq_skripsi.databinding.ActivityEditPurchaseBinding
import com.example.usahaq_skripsi.model.Product
import com.example.usahaq_skripsi.model.Purchase
import com.example.usahaq_skripsi.ui.add.AddPurchaseActivity
import com.example.usahaq_skripsi.util.ViewModelFactory
import com.example.usahaq_skripsi.viewmodel.ProductViewModel
import com.example.usahaq_skripsi.viewmodel.PurchaseViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class EditPurchaseActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditPurchaseBinding
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private var imageUri : Uri?= null
    private var imageUrl : Uri?= null
    private lateinit var imageId: String
    private lateinit var productViewModel: ProductViewModel
    private lateinit var viewmodel: PurchaseViewModel

    private var purchase : Purchase = Purchase()
    private var product : Product = Product()

    private var productExisted : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageId = UUID.randomUUID().toString()

        val purchaseData = intent.getParcelableExtra<Purchase>(PURCHASE)

        val factory = ViewModelFactory.getInstance(this)
        viewmodel = ViewModelProvider(this, factory)[PurchaseViewModel::class.java]
        productViewModel = ViewModelProvider(this, factory)[ProductViewModel::class.java]


        binding.apply {
            etProduct.setText(purchaseData?.name)
            etDate.setText(purchaseData?.date)
            etStocks.setText(purchaseData?.stocks)
            etPrice.setText(purchaseData?.price)
            Glide.with(this@EditPurchaseActivity)
                .load(purchaseData?.imageUrl)
                .into(profilePurchase)
            etDate.setOnClickListener {
                pickDate()
            }
            profilePurchase.setOnClickListener {
                pickImage()
            }

            tvAddPhoto.setOnClickListener {
                pickImage()
            }

            btnAddPurchase.setOnClickListener {
                editPurchase(purchaseData!!)
            }

            btnBack.setOnClickListener {
                onBackPressed()
            }
        }

    }

    private fun editPurchase(purchaseData : Purchase) {
        purchase.name = binding.etProduct.text.toString()
        purchase.price = binding.etPrice.text.toString()
        purchase.stocks = binding.etStocks.text.toString()
        purchase.date = binding.etDate.text.toString()
        if (imageUrl!= null) {
            purchase.imageUrl = imageUrl.toString()
        }
        else{
            purchase.imageUrl = purchaseData.imageUrl
        }
        editProduct(purchaseData)
        purchase.purchaseId = purchaseData.purchaseId
        viewmodel.editPurchase(purchase)
        if(isSuccess){
            finish()
        }
    }

    private fun editProduct(purchaseData: Purchase){
        productViewModel.showListProduct(purchaseData?.businessId!!).observe(this,{result ->
            for(i in result){
                if(binding.etProduct.text.toString().equals(i.name, ignoreCase = true)){
                    productExisted = true
                    product = i
                }
            }
            if(productExisted){
                val stockChanges = binding.etStocks.text.toString().toInt().minus(purchaseData.stocks?.toInt()!!)
                val totalStock = product.stocks?.toInt()?.plus(stockChanges)
                product.price = product.price
                product.stocks = totalStock.toString()
                if (imageUrl!= null) {
                    product.imageUrl = imageUrl.toString()
                }
                productViewModel.editProduct(product)
            }
        }
        )
    }

    private fun pickDate(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in TextView
            binding.etDate.setText("$dayOfMonth - $monthOfYear - $year")
        }, year, month, day)
        dpd.show()
    }

    private fun pickImage() {
        val gallery = Intent()
        gallery.type = "image/*"
        gallery.action = Intent.ACTION_GET_CONTENT
        gallery.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(gallery, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == 101) {
            imageUri = data?.data
            Log.d("GET PICTURE", "Gallery picture : $imageUri")
            storage = FirebaseStorage.getInstance()
            storageReference = storage.getReference("images/ $imageId")
            val uploadTask = storageReference.putFile(imageUri!!)
            uploadTask.addOnSuccessListener {
                storageReference.downloadUrl.addOnCompleteListener {
                    imageUrl = it.result
                    Log.d("UPLOAD PICTURE", "Upload picture: $imageUrl")
                    Glide.with(this).load(imageUrl).into(binding.profilePurchase)
                    Toast.makeText(this, "Image Uploaded", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object{
        var isSuccess = true
        const val PURCHASE = "purchase"
    }
}