package com.example.usahaq_skripsi.util

import com.example.usahaq_skripsi.model.Product
import com.example.usahaq_skripsi.model.ProductSales

fun productInToProductSales(product : List<Product>) : List<ProductSales>{
    var productData = ArrayList<ProductSales>()

    for(i in product){
        productData.add(
            ProductSales(
                name = i.name,
                price = i.price,
                stocks = i.stocks,
                imageUrl = i.imageUrl,
            )
        )
    }
    return productData
}