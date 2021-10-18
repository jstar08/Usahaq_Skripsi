package com.example.usahaq_skripsi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.usahaq_skripsi.databinding.ItemOrdersummaryBinding
import com.example.usahaq_skripsi.model.ProductSales

class CheckoutAdapter () : RecyclerView.Adapter<CheckoutAdapter.ViewHolder>()
{
    var productData: ArrayList<ProductSales> = ArrayList()

    class ViewHolder(val binding: ItemOrdersummaryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(productData: ProductSales) {
            binding.apply {
                tvItem.text = productData.name
                val totalPrice = productData.price?.toInt()?.times(productData.amount?.toInt()!!)
                tvPrice.text = "Rp.$totalPrice"
                tvQuantity.text ="${productData.amount}x"
                if (productData.note==""){
                    tvNote.text = "Note : -"
                }
                else{
                    tvNote.text = productData.note
                }
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemOrdersummaryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productData[position])
    }

    override fun getItemCount(): Int {
        return productData.size
    }
}