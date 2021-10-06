package com.example.usahaq_skripsi.di

import android.content.Context
import com.example.usahaq_skripsi.repo.repository.RemoteDataSource
import com.example.usahaq_skripsi.repo.repository.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object Injection {
    fun provideRepository(context : Context) : Repository {
        val remoteDataSource = RemoteDataSource.getInstance()
        val auth = FirebaseAuth.getInstance()
        val firestore = Firebase.firestore
        return Repository.getInstance(remoteDataSource, auth, firestore, context)
    }
}