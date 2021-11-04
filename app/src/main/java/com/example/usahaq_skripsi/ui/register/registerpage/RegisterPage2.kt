package com.example.usahaq_skripsi.ui.register.registerpage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.usahaq_skripsi.databinding.FragmentRegisterPage2Binding
import com.example.usahaq_skripsi.listener.RegisterActivityListener
import com.example.usahaq_skripsi.ui.register.Register
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.util.*

class RegisterPage2(private val mRegisterActivityListener: RegisterActivityListener) : Fragment() {
    private lateinit var binding: FragmentRegisterPage2Binding
    private var imageUri: Uri? = null
    private var imageUrl: Uri? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var imageId: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterPage2Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRegisterActivityListener.fragment2(binding)
        imageId = UUID.randomUUID().toString()
        binding.apply {
            profileUser.setOnClickListener {
                pickImage()
            }
            tvAddPhoto.setOnClickListener {
                pickImage()
            }
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
            storageReference = storage.getReference("images/$imageId")
            Glide.with(this).load(imageUri).into(binding.profileUser)
            val uploadTask = storageReference.putFile(imageUri!!)
            uploadTask.addOnSuccessListener {
                storageReference.downloadUrl.addOnCompleteListener {
                    imageUrl = it.result
                    Register.imageUrl = imageUrl.toString()
                    Log.d("UPLOAD PICTURE", "Upload picture: $imageUrl")
                }
            }
        }
    }
}