package com.example.usahaq_skripsi.ui.detail.business.tablayout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.usahaq_skripsi.R
import com.example.usahaq_skripsi.adapter.BusinessAdapter
import com.example.usahaq_skripsi.adapter.ProductAdapter
import com.example.usahaq_skripsi.databinding.FragmentProductBinding
import com.example.usahaq_skripsi.util.ViewModelFactory
import com.example.usahaq_skripsi.viewmodel.ProductViewModel
import com.facebook.shimmer.ShimmerFrameLayout


class ProductFragment(private val businessId : String) : Fragment() {

    private lateinit var binding : FragmentProductBinding
    private lateinit var viewmodel : ProductViewModel
    private lateinit var adapter: ProductAdapter
    private lateinit var shimmerRv : ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        showProduct(adapter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        viewmodel = ViewModelProvider(this, factory)[ProductViewModel::class.java]

        shimmerRv = binding.shimmerLayout
        shimmerRv.visibility = View.VISIBLE
        binding.rvProduct.visibility = View.GONE

        adapter = ProductAdapter()

        binding.apply {
            rvProduct.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun showProduct(adapter: ProductAdapter){
        viewmodel.showListProduct(businessId).observe(this, { result ->
            adapter.productData.clear()
            adapter.productData.addAll(result)
            shimmerRv.visibility = View.GONE
            binding.rvProduct.visibility = View.VISIBLE
            binding.rvProduct.adapter = adapter
        })

    }
}