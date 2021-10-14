package com.example.usahaq_skripsi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.usahaq_skripsi.model.Purchase
import com.example.usahaq_skripsi.repo.repository.Repository

class PurchaseViewModel(private val mRepository: Repository) : ViewModel() {

    fun createPurchase(purchase: Purchase) = mRepository.createPurchase(purchase)

    fun showListPurchase(businessId:String) : LiveData<List<Purchase>>
            = mRepository.showListPurchase(businessId)

    fun editPurchase(purchase: Purchase) = mRepository.editPurchase(purchase)

    fun detailPurchase(purchaseId : String) : LiveData<Purchase> = mRepository.detailPurchase(purchaseId)

    fun deletePurchase(purchaseId: String) = mRepository.deletePurchase(purchaseId)
}