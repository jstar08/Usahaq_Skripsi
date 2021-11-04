package com.example.usahaq_skripsi.ui.add.purchase

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.usahaq_skripsi.R
import com.example.usahaq_skripsi.databinding.ActivityAddPurchaseBinding
import com.example.usahaq_skripsi.model.Business
import com.example.usahaq_skripsi.model.Product
import com.example.usahaq_skripsi.model.Purchase
import com.example.usahaq_skripsi.util.ViewModelFactory
import com.example.usahaq_skripsi.viewmodel.ProductViewModel
import com.example.usahaq_skripsi.viewmodel.PurchaseViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class AddPurchaseActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddPurchaseBinding
    private var businessData : Business?= null
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private var imageUri : Uri?= null
    private var imageUrl : Uri?= null
    private lateinit var imageId: String
    private lateinit var viewmodel : PurchaseViewModel
    private lateinit var productViewModel: ProductViewModel

    private var purchase : Purchase = Purchase()
    private var product : Product = Product()

    private var productExisted : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageId = UUID.randomUUID().toString()

        val factory = ViewModelFactory.getInstance(this)
        viewmodel = ViewModelProvider(this, factory)[PurchaseViewModel::class.java]
        productViewModel = ViewModelProvider(this, factory)[ProductViewModel::class.java]

        businessData = intent.getParcelableExtra(BUSINESS)

        binding.apply {
            etSales.visibility = View.GONE
            ivSell.visibility = View.GONE
            etDesc.visibility = View.GONE
            ivDesc.visibility = View.GONE
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
                if(!isFieldEmpty()) addPurchase()
            }

            btnBack.setOnClickListener {
                onBackPressed()
            }
            switchButton.setOnClickListener {
                showProductPrice()
            }
        }
    }

    private fun isFieldEmpty() : Boolean{
        binding.apply {
            if(etProduct.text.isEmpty()){
                etProduct.error = getString(R.string.emptyField)
                return true
            }
            if (imageUri==null){
                Toast.makeText(this@AddPurchaseActivity, getString(R.string.emptyPict), Toast.LENGTH_SHORT).show()
                return true
            }

            if(etStocks.text.isEmpty()){
                etStocks.error = getString(R.string.emptyField)
                return true
            }
            if (etPrice.text.isEmpty()){
                etPrice.error = getString(R.string.emptyField)
                return true
            }
            if(etDate.text.isEmpty()){
                etDate.error = getString(R.string.emptyField)
                return true
            }
            if(switchButton.isChecked){
                if(etDesc.text.isEmpty()){
                    etDesc.error = getString(R.string.emptyField)
                    return true
                }
                if(etSales.text.isEmpty()){
                    etSales.error = getString(R.string.emptyField)
                    return true
                }
                else return false
            }
            else return false
        }
    }

    private fun addPurchase(){
        purchase.name = binding.etProduct.text.toString()
        purchase.stocks = binding.etStocks.text.toString()
        purchase.price = binding.etPrice.text.toString()
        purchase.imageUrl = imageUrl.toString()
        purchase.date = binding.etDate.text.toString()
        purchase.purchaseId = UUID.randomUUID().toString()
        purchase.businessId = businessData?.businessId
        viewmodel.createPurchase(purchase)

        if (binding.switchButton.isChecked){
            addProduct()
        }
        if(isSuccess){
            finish()
        }
    }

    private fun showProductPrice(){
        binding.apply {
            if (switchButton.isChecked){
                etSales.visibility = View.VISIBLE
                ivSell.visibility = View.VISIBLE
                etDesc.visibility = View.VISIBLE
                ivDesc.visibility = View.VISIBLE
            }
            else{
                etSales.visibility = View.GONE
                ivSell.visibility = View.GONE
                etDesc.visibility = View.GONE
                ivDesc.visibility = View.GONE
            }
        }


    }

    private fun addProduct(){
        productViewModel.showListProduct(businessData?.businessId!!).observe(this,{result ->
            for(i in result){
                if(binding.etProduct.text.toString().equals(i.name, ignoreCase = true)){
                    productExisted = true
                    product = i
                }
            }
            if(productExisted){
                val totalStock = product.stocks?.toInt()?.plus(binding.etStocks.text.toString().toInt())
                product.price = binding.etSales.text.toString()
                product.stocks = totalStock.toString()
                binding.etDesc.setText(product.description)
                product.description = binding.etDesc.text.toString()
                product.imageUrl = imageUrl.toString()
                productViewModel.editProduct(product)
            }
            else {
                product.name = binding.etProduct.text.toString()
                product.stocks = binding.etStocks.text.toString()
                product.price = binding.etSales.text.toString()
                product.imageUrl = imageUrl.toString()
                product.description = binding.etDesc.text.toString()
                product.productId = UUID.randomUUID().toString()
                product.businessId = businessData?.businessId
                productViewModel.createProduct(product)
            }
        })


    }

    private fun pickDate(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in TextView
            binding.etDate.setText("$dayOfMonth-${monthOfYear.plus(1)}-$year")
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
        const val BUSINESS = "business"
    }
}