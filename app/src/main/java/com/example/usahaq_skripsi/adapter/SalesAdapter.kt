package com.example.usahaq_skripsi.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.usahaq_skripsi.R
import com.example.usahaq_skripsi.databinding.ItemProductRecommendBinding
import com.example.usahaq_skripsi.model.Sales
import com.example.usahaq_skripsi.ui.detail.purchase.DetailPurchaseActivity
import com.example.usahaq_skripsi.ui.detail.sales.DetailSalesActivity

class SalesAdapter : RecyclerView.Adapter<SalesAdapter.ViewHolder>() {
    var salesData: ArrayList<Sales> = ArrayList()

    class ViewHolder(val binding: ItemProductRecommendBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(salesData: Sales) {
            val context = binding.root.context
            binding.apply {
                tvProductName.text = salesData.salesId
                tvPrice.text = salesData.totalPrice
            }
            Glide.with(context).load(R.drawable.saleslogo).into(binding.ivProduct)

            itemView.setOnClickListener {
                val intent = Intent(context, DetailSalesActivity::class.java)
                intent.putExtra(DetailSalesActivity.SALES_ID, salesData.salesId)
                context.startActivity(intent)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemProductRecommendBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(salesData[position])
    }

    override fun getItemCount(): Int {
        return salesData.size
    }
}