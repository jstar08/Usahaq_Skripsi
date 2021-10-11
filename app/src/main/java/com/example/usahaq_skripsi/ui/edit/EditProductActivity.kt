package com.example.usahaq_skripsi.ui.edit

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
import com.example.usahaq_skripsi.databinding.ActivityEditProductBinding
import com.example.usahaq_skripsi.model.Business
import com.example.usahaq_skripsi.model.Product
import com.example.usahaq_skripsi.ui.dashboard.DashboardActivity
import com.example.usahaq_skripsi.ui.detail.business.DetailBusinessActivity
import com.example.usahaq_skripsi.util.ViewModelFactory
import com.example.usahaq_skripsi.viewmodel.ProductViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class EditProductActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditProductBinding
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private var imageUri : Uri?= null
    private var imageUrl : Uri?= null
    private lateinit var imageId: String
    private lateinit var viewModel: ProductViewModel

    private var product : Product = Product()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this,factory)[ProductViewModel::class.java]

        imageId = UUID.randomUUID().toString()

        val productData = intent.getParcelableExtra<Product>(PRODUCT)

        binding.apply {
            etDesc.setText(productData?.description)
            etProduct.setText(productData?.name)
            etSell.setText(productData?.price)
            etStocks.setText(productData?.stocks)
            Glide.with(this@EditProductActivity)
                .load(productData?.imageUrl)
                .into(profileProduct)

            btnEditProduct.setOnClickListener {
                editProduct(productData!!)
            }

            profileProduct.setOnClickListener {
                pickImage()
            }

            tvAddPhoto.setOnClickListener {
                pickImage()
            }
            btnBack.setOnClickListener { onBackPressed() }
        }

    }

    private fun editProduct(productData: Product){
        product.name = binding.etProduct.text.toString()
        product.price = binding.etSell.text.toString()
        product.stocks = binding.etStocks.text.toString()
        product.description = binding.etDesc.text.toString()
        if (imageUrl!= null) {
            product.imageUrl = imageUrl.toString()
        }
        else{
            product.imageUrl = productData.imageUrl
        }
        product.productId = productData.productId
        viewModel.editProduct(product)
        if(isSuccess){
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
        var isSuccess = true
        const val PRODUCT = "product"
    }
}