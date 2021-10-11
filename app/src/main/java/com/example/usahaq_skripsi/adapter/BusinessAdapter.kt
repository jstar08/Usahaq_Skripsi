package com.example.usahaq_skripsi.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.usahaq_skripsi.databinding.ItemBusinessBinding
import com.example.usahaq_skripsi.model.Business
import com.example.usahaq_skripsi.ui.detail.business.DetailBusinessActivity

class BusinessAdapter : RecyclerView.Adapter<BusinessAdapter.ViewHolder>() {
    var businessData: ArrayList<Business> = ArrayList()

    class ViewHolder(val binding: ItemBusinessBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(businessData: Business) {
            val context = binding.root.context
            binding.tvBusinessName.text = businessData.name
            binding.tvBusinessLocation.text = businessData.address
            Glide.with(context).load(businessData.imageUrl).into(binding.ivPoster)

            itemView.setOnClickListener {
                val intent = Intent(context, DetailBusinessActivity::class.java)
                intent.putExtra(DetailBusinessActivity.BUSINESS_ID, businessData.businessId)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBusinessBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(businessData[position])
    }

    override fun getItemCount(): Int {
        return businessData.size
    }
}
