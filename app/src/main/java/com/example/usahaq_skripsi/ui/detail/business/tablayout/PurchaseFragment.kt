package com.example.usahaq_skripsi.ui.detail.business.tablayout

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.usahaq_skripsi.R
import com.example.usahaq_skripsi.adapter.ProductAdapter
import com.example.usahaq_skripsi.adapter.PurchaseAdapter
import com.example.usahaq_skripsi.databinding.FragmentPurchaseBinding
import com.example.usahaq_skripsi.model.Purchase
import com.example.usahaq_skripsi.model.Sales
import com.example.usahaq_skripsi.util.ViewModelFactory
import com.example.usahaq_skripsi.viewmodel.PurchaseViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PurchaseFragment (private val businessId : String) : Fragment() {

    private lateinit var binding : FragmentPurchaseBinding
    private lateinit var viewModel: PurchaseViewModel
    private lateinit var adapter : PurchaseAdapter
    private lateinit var shimmerRv : ShimmerFrameLayout

    override fun onResume() {
        super.onResume()
        showProduct(adapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPurchaseBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[PurchaseViewModel::class.java]

        shimmerRv = binding.shimmerLayout
        shimmerRv.visibility = View.VISIBLE
        binding.rvPurchase.visibility = View.GONE

        adapter = PurchaseAdapter()

        binding.apply {
            rvPurchase.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun showProduct(adapter: PurchaseAdapter){
        val dateTimeFormatter: DateTimeFormatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter.ofPattern("d-MM-yyyy")
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        viewModel.showListPurchase(businessId).observe(this, { result ->
            adapter.purchaseData.clear()
            adapter.purchaseData.addAll(result)
            adapter.purchaseData.sortByDescending { purchaseData : Purchase ->
                LocalDate.parse(purchaseData.date, dateTimeFormatter)}
            shimmerRv.visibility = View.GONE
            binding.rvPurchase.visibility = View.VISIBLE
            binding.rvPurchase.adapter = adapter
        })

    }

}