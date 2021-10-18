package com.example.usahaq_skripsi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.usahaq_skripsi.databinding.ItemOrdersummaryBinding
import com.example.usahaq_skripsi.model.ProductSales
import com.example.usahaq_skripsi.model.ProductSold

class SalesDetailAdapter () : RecyclerView.Adapter<SalesDetailAdapter.ViewHolder>()
{
    var productData: ArrayList<ProductSold> = ArrayList()

    class ViewHolder(val binding: ItemOrdersummaryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(productData: ProductSold) {
            val context = binding.root.context
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