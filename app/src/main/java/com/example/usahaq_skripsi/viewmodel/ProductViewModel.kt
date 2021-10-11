package com.example.usahaq_skripsi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.usahaq_skripsi.adapter.BusinessAdapter
import com.example.usahaq_skripsi.adapter.ProductAdapter
import com.example.usahaq_skripsi.model.Business
import com.example.usahaq_skripsi.model.Product
import com.example.usahaq_skripsi.repo.repository.Repository

class ProductViewModel(private val mRepository: Repository) : ViewModel() {

    fun createProduct(product: Product) = mRepository.createProduct(product)

    fun showListProduct(businessId:String, adapter: ProductAdapter) : LiveData<List<Product>>
    = mRepository.showListProduct(businessId,adapter)

    fun editProduct(product: Product) = mRepository.editProduct(product)

    fun detailProduct(productId : String) : LiveData<Product> = mRepository.detailProduct(productId)

    fun deleteProduct(productId : String) = mRepository.deleteProduct(productId)
}