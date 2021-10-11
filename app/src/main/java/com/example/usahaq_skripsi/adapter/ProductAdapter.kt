package com.example.usahaq_skripsi.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.usahaq_skripsi.databinding.ItemProductRecommendBinding
import com.example.usahaq_skripsi.model.Product
import com.example.usahaq_skripsi.ui.detail.product.DetailProductActivity

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    var productData: ArrayList<Product> = ArrayList()

    class ViewHolder(val binding: ItemProductRecommendBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(productData: Product) {
            val context = binding.root.context
            binding.apply {
                tvProductName.text = productData.name
                tvPrice.text = "${productData.stocks} pcs"
            }
            Glide.with(context).load(productData.imageUrl).into(binding.ivProduct)

            itemView.setOnClickListener {
                val intent = Intent(context, DetailProductActivity::class.java)
                intent.putExtra(DetailProductActivity.PRODUCT_ID, productData.productId)
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
        holder.bind(productData[position])
    }

    override fun getItemCount(): Int {
        return productData.size
    }
}