package com.example.usahaq_skripsi.ui.add.product

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.usahaq_skripsi.databinding.ActivityAddProductBinding
import com.example.usahaq_skripsi.model.Business
import com.example.usahaq_skripsi.model.Product
import com.example.usahaq_skripsi.ui.add.business.AddBusinessActivity
import com.example.usahaq_skripsi.util.ViewModelFactory
import com.example.usahaq_skripsi.viewmodel.ProductViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class AddProductActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddProductBinding
    private var businessData : Business?= null
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private var imageUri : Uri?= null
    private var imageUrl : Uri?= null
    private lateinit var imageId: String
    private lateinit var viewmodel : ProductViewModel

    private var product : Product = Product()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageId = UUID.randomUUID().toString()

        val factory = ViewModelFactory.getInstance(this)
        viewmodel = ViewModelProvider(this, factory)[ProductViewModel::class.java]

        businessData = intent.getParcelableExtra(BUSINESS_DATA)

        binding.apply {
            profileProduct.setOnClickListener {
                pickImage()
            }

            tvAddPhoto.setOnClickListener {
                pickImage()
            }

            btnAddProduct.setOnClickListener {
                addProduct()
            }

            btnBack.setOnClickListener {
                onBackPressed()
            }
        }

    }

    private fun addProduct(){
        product.name = binding.etProduct.text.toString()
        product.stocks = binding.etStocks.text.toString()
        product.price = binding.etSell.text.toString()
        product.description = binding.etDesc.text.toString()
        product.imageUrl = imageUrl.toString()
        product.productId = UUID.randomUUID().toString()
        product.businessId = businessData?.businessId
        viewmodel.createProduct(product)
        if(AddBusinessActivity.isSuccess){
            finish()
        }
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
                    Glide.with(this).load(imageUrl).into(binding.profileProduct)
                    Toast.makeText(this, "Image Uploaded", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    companion object{
        const val BUSINESS_DATA = "business_data"
        var isSuccess = true
    }
}