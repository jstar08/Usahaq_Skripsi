package com.example.usahaq_skripsi.viewmodel

import androidx.lifecycle.ViewModel
import com.example.usahaq_skripsi.model.Account
import com.example.usahaq_skripsi.repo.repository.Repository

class AuthViewModel(private val mRepository: Repository) : ViewModel() {

    fun createAccount(account: Account) = mRepository.createAccount(account)

    fun signIn(email: String, password : String) = mRepository.signIn(email, password)

    fun getAccountData() = mRepository.showUserProfile()
}