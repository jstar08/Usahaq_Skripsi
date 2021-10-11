package com.example.usahaq_skripsi.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Business(
    var businessId : String ?= null,
    var userId : String?= null,
    var name : String?= null,
    var address : String?= null,
    var imageUrl : String?= null
) :Parcelable
