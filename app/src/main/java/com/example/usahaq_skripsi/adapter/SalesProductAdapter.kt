package com.example.usahaq_skripsi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.usahaq_skripsi.databinding.ItemTransactionproductBinding
import com.example.usahaq_skripsi.model.ProductSales

class SalesProductAdapter(val listener: OnClickListener) : RecyclerView.Adapter<SalesProductAdapter.ViewHolder>() {
    var productData: ArrayList<ProductSales> = ArrayList()

    class ViewHolder(val binding: ItemTransactionproductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(productData: ProductSales, listener: OnClickListener, position: Int) {
            val context = binding.root.context
            binding.apply {
                tvProduct.text = productData.name
                tvDescription.text = "Rp ${productData.price}"
                if(productData.amount==null || productData.amount == "0"){
                    ivNumberchat.visibility = View.GONE
                    tvNumber.visibility = View.GONE
                }
                else{
                    ivNumberchat.visibility = View.VISIBLE
                    tvNumber.visibility = View.VISIBLE
                    tvNumber.text = productData.amount
                }
            }
            Glide.with(context).load(productData.imageUrl).into(binding.ivFoodimage)
            itemView.setOnClickListener {
                listener.onClick(productData, position)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTransactionproductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productData[position], listener, position)
    }

    override fun getItemCount(): Int {
        return productData.size
    }

    interface OnClickListener {
        fun onClick(productData: ProductSales, index : Int)
    }
}