package com.example.usahaq_skripsi.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.usahaq_skripsi.di.Injection
import com.example.usahaq_skripsi.repo.repository.Repository
import com.example.usahaq_skripsi.viewmodel.AuthViewModel
import com.example.usahaq_skripsi.viewmodel.BusinessViewModel

class ViewModelFactory private constructor(private val mContentRepository: Repository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(mContentRepository) as T
            }
            modelClass.isAssignableFrom(BusinessViewModel::class.java) -> {
                BusinessViewModel(mContentRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel Class : " + modelClass.name)
        }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context : Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }
    }
}