package com.example.usahaq_skripsi.repo.response

import com.google.gson.annotations.SerializedName

data class MachineLearningResponse(
    @field:SerializedName("result") val result: ArrayList<Float>
)
