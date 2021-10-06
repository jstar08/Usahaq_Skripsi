package com.example.usahaq_skripsi.repo.api

import com.example.usahaq_skripsi.repo.response.MachineLearningResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ApiService {
    @GET("machine_learning/{type}")
    fun getPrediction(
        @Path(value = "type", encoded = true) type: String
    ): Call<MachineLearningResponse>
}