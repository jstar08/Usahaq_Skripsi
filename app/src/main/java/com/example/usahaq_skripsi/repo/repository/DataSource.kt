package com.example.usahaq_skripsi.repo.repository

import androidx.lifecycle.LiveData
import com.example.usahaq_skripsi.adapter.BusinessAdapter
import com.example.usahaq_skripsi.adapter.ProductAdapter
import com.example.usahaq_skripsi.adapter.PurchaseAdapter
import com.example.usahaq_skripsi.model.Account
import com.example.usahaq_skripsi.model.Business
import com.example.usahaq_skripsi.model.Product
import com.example.usahaq_skripsi.model.Purchase

interface DataSource {

    fun createAccount(account : Account)

    fun signIn(email : String, password : String)

    fun createBusiness(business: Business)

    fun showlistBusiness(adapter: BusinessAdapter) : LiveData<List<Business>>

    fun DetailBusiness(businessId: String) : LiveData<Business>

    fun editBusiness(business: Business)

    fun showUserProfile() : LiveData<Account>

    fun createProduct(product : Product)

    fun showListProduct(businessId: String) : LiveData<List<Product>>

    fun editProduct(product : Product)

    fun detailProduct(productId : String) : LiveData<Product>

    fun deleteProduct(productId : String)

    fun createPurchase(purchase: Purchase)

    fun showListPurchase(businessId : String) : LiveData<List<Purchase>>

    fun editPurchase(purchase: Purchase)

    fun detailPurchase(purchaseId : String) : LiveData<Purchase>

    fun deletePurchase(purchaseId: String)
}