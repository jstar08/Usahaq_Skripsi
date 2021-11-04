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
import com.example.usahaq_skripsi.R
import com.example.usahaq_skripsi.databinding.ActivityEditBusinessBinding
import com.example.usahaq_skripsi.model.Business
import com.example.usahaq_skripsi.util.ViewModelFactory
import com.example.usahaq_skripsi.viewmodel.BusinessViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class EditBusinessActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditBusinessBinding
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private var imageUri : Uri?= null
    private var imageUrl : Uri?= null
    private lateinit var imageId: String
    private lateinit var viewModel: BusinessViewModel

    private var business : Business = Business()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBusinessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[BusinessViewModel::class.java]

        val businessData = intent.getParcelableExtra<Business>(BUSINESS)

        imageId = UUID.randomUUID().toString()

        binding.apply {
            etAddress.setText(businessData?.address)
            etBusiness.setText(businessData?.name)
            Glide.with(this@EditBusinessActivity)
                .load(businessData?.imageUrl)
                .into(binding.profileBusiness)

            profileBusiness.setOnClickListener {
                pickImage()
            }

            tvAddPhoto.setOnClickListener {
                pickImage()
            }

            btnEditBusiness.setOnClickListener {
                if (!isEmpty()) editBusiness(businessData!!)
            }
            btnBack.setOnClickListener { onBackPressed() }
        }

    }

    private fun editBusiness(businessData: Business){
        business.name = binding.etBusiness.text.toString()
        business.address = binding.etAddress.text.toString()
        if (imageUrl!= null) {
            business.imageUrl = imageUrl.toString()
        }
        else{
            business.imageUrl = businessData.imageUrl
        }
        business.businessId = businessData.businessId
        viewModel.editBusiness(business)
        if(isSuccess){
            finish()
        }
    }

    private fun isEmpty() : Boolean {
        binding.apply {
            if(etBusiness.text.trim().isEmpty() && etAddress.text.trim().isEmpty()){
                etBusiness.error = "This field can't be empty"
                etAddress.error = "This field can't be empty"
                return true
            } else if(etBusiness.text.trim().isEmpty()) {
                etBusiness.error = "This field can't be empty"
                return true
            } else if(etAddress.text.trim().isEmpty()){
                etAddress.error = "This field can't be empty"
                return true
            }
            else return false
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
                    Glide.with(this).load(imageUrl).into(binding.profileBusiness)
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