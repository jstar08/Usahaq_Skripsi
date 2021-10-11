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


class ProductFragment(private val businessId : String) : Fragment() {

    private lateinit var binding : FragmentProductBinding
    private lateinit var viewmodel : ProductViewModel
    private lateinit var adapter: ProductAdapter

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

        adapter = ProductAdapter()

        binding.apply {
            rvProduct.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun showProduct(adapter: ProductAdapter){
        viewmodel.showListProduct(businessId,adapter).observe(this, { result ->
            adapter.productData.clear()
            adapter.productData.addAll(result)
            binding.rvProduct.adapter = adapter
        })

    }
}