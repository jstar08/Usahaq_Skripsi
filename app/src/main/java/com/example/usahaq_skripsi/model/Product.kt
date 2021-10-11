package com.example.usahaq_skripsi.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    var productId : String ?= null,
    var businessId : String ?= null,
    var name : String ?= null,
    var price : String ?= null,
    var stocks : String ?= null,
    var imageUrl : String?= null,
    var description : String?= null
) :Parcelable
