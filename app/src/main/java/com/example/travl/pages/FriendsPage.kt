package com.example.travl.pages

import FriendsViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travl.adapters.FriendsPageItemAdapter
import com.example.travl.databinding.FriendsPageBinding

class FriendsPage : Fragment() {
    private lateinit var binding: FriendsPageBinding
    private val viewModel: FriendsViewModel by viewModels()
    private lateinit var adapter: FriendsPageItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FriendsPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FriendsPageItemAdapter(requireContext())

        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.friendsRecycler.layoutManager =
            manager

        viewModel.dataList.observe(viewLifecycleOwner) { data ->
            adapter.data = data
        }

        viewModel.loadData()

        binding.friendsRecycler.adapter = adapter

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.friendRequests.setOnClickListener {
            findNavController().navigate(FriendsPageDirections.actionFriendsPageToFriendsRequestsPage())
        }


    }
}