package com.example.usahaq_skripsi.ui.detail.business.tablayout

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.usahaq_skripsi.R
import com.example.usahaq_skripsi.adapter.PurchaseAdapter
import com.example.usahaq_skripsi.adapter.SalesAdapter
import com.example.usahaq_skripsi.databinding.FragmentPurchaseBinding
import com.example.usahaq_skripsi.databinding.FragmentSalesBinding
import com.example.usahaq_skripsi.model.Sales
import com.example.usahaq_skripsi.util.ViewModelFactory
import com.example.usahaq_skripsi.viewmodel.PurchaseViewModel
import com.example.usahaq_skripsi.viewmodel.SalesViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class SalesFragment (private val businessId : String) : Fragment() {

    private lateinit var binding : FragmentSalesBinding
    private lateinit var viewModel: SalesViewModel
    private lateinit var adapter : SalesAdapter
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
        binding = FragmentSalesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SalesAdapter()
        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[SalesViewModel::class.java]

        shimmerRv = binding.shimmerLayout
        shimmerRv.visibility = View.VISIBLE
        binding.rvSales.visibility = View.GONE

        binding.apply {
            rvSales.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun showProduct(adapter: SalesAdapter){
        val dateTimeFormatter: DateTimeFormatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter.ofPattern("d-M-yyyy, h:mm a")
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        viewModel.showListSales(businessId).observe(this, { result ->
            adapter.salesData.clear()
            adapter.salesData.addAll(result)
            adapter.salesData.sortByDescending { salesData : Sales ->
                LocalDate.parse(salesData.date, dateTimeFormatter) }
            shimmerRv.visibility = View.GONE
            binding.rvSales.visibility = View.VISIBLE
            binding.rvSales.adapter = adapter
            Log.d("SIZE", adapter.salesData.size.toString())
        })

    }

}