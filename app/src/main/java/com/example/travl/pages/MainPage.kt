package com.example.travl.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travl.interfaces.OnChildItemClickListener
import com.example.travl.adapters.MainPageParentItemAdapter
import com.example.travl.databinding.MainPageBinding
import com.example.travl.ViewModels.MainPageViewModel


class MainPage : Fragment(), OnChildItemClickListener {
    private lateinit var binding: MainPageBinding
    private lateinit var adapter: MainPageParentItemAdapter
    private val viewModel: MainPageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = MainPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MainPageParentItemAdapter(requireContext(), this)


        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.mainPageRecycler.layoutManager =
            manager


        binding.mainPageRecycler.adapter = adapter

        viewModel.dataList.observe(viewLifecycleOwner) { data ->
            adapter.data = data
        }

        viewModel.loadData()

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

    override fun onChildItemClick(parentPosition: Int, childPosition: Int) {
        val childItem = adapter.getChildItem(parentPosition, childPosition)

        val action = MainPageDirections.actionMainPageToPlacePage(
            childItem.imageResURL,
            childItem.placeName,
            childItem.regionName,
            childItem.description
        )


        findNavController().navigate(action)
    }
}
