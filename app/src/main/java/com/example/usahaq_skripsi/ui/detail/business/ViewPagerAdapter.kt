package com.example.usahaq_skripsi.ui.detail.business

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.usahaq_skripsi.ui.detail.business.tablayout.ProductFragment
import com.example.usahaq_skripsi.ui.detail.business.tablayout.PurchaseFragment
import com.example.usahaq_skripsi.ui.detail.business.tablayout.SalesFragment

class ViewPagerAdapter(
    activity : AppCompatActivity,
    private val businessId : String
) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProductFragment(businessId)
            1 -> PurchaseFragment(businessId)
            2 -> SalesFragment(businessId)
            else -> ProductFragment(businessId)
        }
    }

}