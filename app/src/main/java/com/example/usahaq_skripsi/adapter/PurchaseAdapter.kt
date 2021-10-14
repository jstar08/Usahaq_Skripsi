package com.example.usahaq_skripsi.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.usahaq_skripsi.databinding.ItemProductRecommendBinding
import com.example.usahaq_skripsi.model.Purchase
import com.example.usahaq_skripsi.ui.detail.product.DetailProductActivity
import com.example.usahaq_skripsi.ui.detail.purchase.DetailPurchaseActivity

class PurchaseAdapter : RecyclerView.Adapter<PurchaseAdapter.ViewHolder>() {
    var purchaseData: ArrayList<Purchase> = ArrayList()

    class ViewHolder(val binding: ItemProductRecommendBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(purchaseData: Purchase) {
            val context = binding.root.context
            binding.apply {
                tvProductName.text = purchaseData.name
                tvPrice.text = "Rp ${purchaseData.price}"
            }
            Glide.with(context).load(purchaseData.imageUrl).into(binding.ivProduct)

            itemView.setOnClickListener {
                val intent = Intent(context, DetailPurchaseActivity::class.java)
                intent.putExtra(DetailPurchaseActivity.PURCHASE_ID, purchaseData.purchaseId)
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
        holder.bind(purchaseData[position])
    }

    override fun getItemCount(): Int {
        return purchaseData.size
    }
}