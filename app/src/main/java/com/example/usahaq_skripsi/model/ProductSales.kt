package com.example.usahaq_skripsi.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductSales(
    var name : String ?= null,
    var price : String ?= null,
    var amount : String ?= null,
    var note : String?= null,
    var stocks : String?=null,
    var imageUrl : String?= null,
) : Parcelable