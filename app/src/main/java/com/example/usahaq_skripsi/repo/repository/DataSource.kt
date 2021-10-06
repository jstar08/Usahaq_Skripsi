package com.example.usahaq_skripsi.repo.repository

import androidx.lifecycle.LiveData
import com.example.usahaq_skripsi.adapter.BusinessAdapter
import com.example.usahaq_skripsi.model.Account
import com.example.usahaq_skripsi.model.Business

interface DataSource {

    fun createAccount(account : Account)

    fun signIn(email : String, password : String)

    fun createBusiness(business: Business)

    fun showlistBusiness(adapter: BusinessAdapter) : LiveData<List<Business>>

    fun showDetailBusiness(businessId : Int)

    fun showUserProfile() : LiveData<Account>
}