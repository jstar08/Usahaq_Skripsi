package com.example.usahaq_skripsi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.usahaq_skripsi.adapter.BusinessAdapter
import com.example.usahaq_skripsi.model.Business
import com.example.usahaq_skripsi.repo.repository.Repository

class BusinessViewModel(private val mRepository: Repository) : ViewModel() {

    fun createBusiness(business: Business) = mRepository.createBusiness(business)

    fun showlistBusiness(adapter: BusinessAdapter) : LiveData<List<Business>> = mRepository.showlistBusiness(adapter)

    fun showDetailBusiness(businessId : Int) = mRepository.showDetailBusiness(businessId)
}