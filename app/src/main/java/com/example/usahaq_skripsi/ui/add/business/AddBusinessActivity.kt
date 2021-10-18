package com.example.usahaq_skripsi.ui.add.business

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
import com.example.usahaq_skripsi.databinding.ActivityAddBusinessBinding
import com.example.usahaq_skripsi.model.Business
import com.example.usahaq_skripsi.util.ViewModelFactory
import com.example.usahaq_skripsi.viewmodel.BusinessViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class AddBusinessActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddBusinessBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseStorage
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private var imageUri : Uri?= null
    private var imageUrl : Uri?= null
    private lateinit var imageId: String
    private lateinit var viewModel : BusinessViewModel

    private var business : Business = Business()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBusinessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseStorage.getInstance()

        imageId = UUID.randomUUID().toString()

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[BusinessViewModel::class.java]

        binding.apply {

            btnAddBusiness.setOnClickListener {
                addBusiness()
            }

            profileBusiness.setOnClickListener {
                pickImage()
            }

            tvAddPhoto.setOnClickListener {
                pickImage()
            }

            btnBack.setOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun addBusiness(){
        business.name = binding.etBusiness.text.toString()
        business.address = binding.etAddress.text.toString()
        business.imageUrl = imageUrl.toString()
        business.userId = auth.currentUser?.uid.toString()
        business.businessId = UUID.randomUUID().toString()
        viewModel.createBusiness(business)
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
                    Glide.with(this).load(imageUrl).into(binding.profileBusiness)
                    Toast.makeText(this, "Image Uploaded", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object{
        var isSuccess = true
    }
}