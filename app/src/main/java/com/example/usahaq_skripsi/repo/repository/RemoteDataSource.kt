package com.example.usahaq_skripsi.repo.repository

import android.util.Log
import com.example.usahaq_skripsi.repo.api.ApiConfig
import com.example.usahaq_skripsi.repo.response.MachineLearningResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource {

    fun getPrediction(type: String,callback: RequestCallback) {
        ApiConfig.getApiService().getPrediction(type).enqueue(object : Callback<MachineLearningResponse>{
            override fun onResponse(
                call: Call<MachineLearningResponse>,
                response: Response<MachineLearningResponse>
            ) {
                callback.onAllMachineLearningReceived(response.body()!!)
            }

            override fun onFailure(call: Call<MachineLearningResponse>, t: Throwable) {
                Log.d(TAG, t.printStackTrace().toString())
            }

        })
    }

    companion object {
        private val TAG = RemoteDataSource::class.java.simpleName

        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource().apply { instance = this }
            }
    }

    interface RequestCallback {
        fun onAllMachineLearningReceived(price : MachineLearningResponse)
    }
}