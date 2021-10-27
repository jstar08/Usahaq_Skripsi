package com.example.usahaq_skripsi.repo.repository

import androidx.lifecycle.LiveData
import com.example.usahaq_skripsi.adapter.BusinessAdapter
import com.example.usahaq_skripsi.adapter.ProductAdapter
import com.example.usahaq_skripsi.adapter.PurchaseAdapter
import com.example.usahaq_skripsi.model.*

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

    fun searchProduct(query: String, businessId: String) : LiveData<List<Product>>

    fun editProduct(product : Product)

    fun detailProduct(productId : String) : LiveData<Product>

    fun deleteProduct(productId : String)

    fun createPurchase(purchase: Purchase)

    fun showListPurchase(businessId : String) : LiveData<List<Purchase>>

    fun editPurchase(purchase: Purchase)

    fun detailPurchase(purchaseId : String) : LiveData<Purchase>

    fun deletePurchase(purchaseId: String)

    fun createSales(sales : Sales, productSold : ArrayList<ProductSold>)

    fun deleteSales(salesId: String)

    fun showListSales(businessId: String) : LiveData<List<Sales>>

    fun detailSales(salesId: String) : LiveData<Sales>

    fun detailProductSold(salesId: String) : LiveData<List<ProductSold>>
}