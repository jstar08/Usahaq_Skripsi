package com.example.usahaq_skripsi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.usahaq_skripsi.model.ProductSold
import com.example.usahaq_skripsi.model.Sales
import com.example.usahaq_skripsi.repo.repository.Repository

class SalesViewModel(private val mRepository: Repository) : ViewModel() {

    fun addSales(sales : Sales, productSold : ArrayList<ProductSold>) =
        mRepository.createSales(sales, productSold)

    fun deleteSales(salesId: String) = mRepository.deleteSales(salesId)

    fun showListSales(businessId: String) : LiveData<List<Sales>> =
        mRepository.showListSales(businessId)

    fun detailSales(salesId: String) : LiveData<Sales> =
        mRepository.detailSales(salesId)

    fun detailProductSold(salesId: String) : LiveData<List<ProductSold>> =
        mRepository.detailProductSold(salesId)
}