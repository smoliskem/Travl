package com.example.travl.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travl.R
import com.example.travl.adapters.MainPageParentItemAdapter
import com.example.travl.databinding.MainPageBinding
import com.example.travl.items.PlaceCardGenerator

class MainPage : Fragment() {
    private lateinit var binding: MainPageBinding
    private lateinit var adapter: MainPageParentItemAdapter
    private val placeCardGenerator: PlaceCardGenerator = PlaceCardGenerator()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MainPageBinding.bind(view)

        adapter = MainPageParentItemAdapter(requireContext())


        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.mainPageRecycler.layoutManager =
            manager

        binding.mainPageRecycler.adapter = adapter

        placeCardGenerator.setDocuments(adapter)

        binding.myPlansBtn.setOnClickListener {
            findNavController().navigate(MainPageDirections.actionMainPageToMyPlansPage())
        }

        binding.jointPlansBtn.setOnClickListener {
            findNavController().navigate(MainPageDirections.actionMainPageToJointPlansPage())
        }

        binding.profileBtn.setOnClickListener {
            findNavController().navigate(MainPageDirections.actionMainPageToProfilePage())
        }
    }
}

