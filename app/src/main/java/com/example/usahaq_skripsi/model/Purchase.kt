package com.example.usahaq_skripsi.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Purchase(
    var purchaseId : String ?=null,
    var businessId : String ?=null,
    var name : String ?= null,
    var price : String ?=null,
    var stocks : String ?= null,
    var date : String ?= null,
    var imageUrl : String ?=null
) : Parcelable
