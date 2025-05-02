package com.example.travl.pages

import FriendsRequestsViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travl.adapters.FriendsRequestPageItemAdapter
import com.example.travl.databinding.FriendsRequestPageBinding
import com.example.travl.viewModels.MyPlansPageViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class FriendsRequestsPage : Fragment() {
    private lateinit var binding: FriendsRequestPageBinding
    private val uid = Firebase.auth.currentUser?.uid
    private val db = FirebaseFirestore.getInstance()
    private val viewModel: FriendsRequestsViewModel by viewModels()
    private lateinit var adapter: FriendsRequestPageItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FriendsRequestPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FriendsRequestPageItemAdapter(requireContext())

        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.friendsRequestsRecycler.layoutManager =
            manager

        viewModel.dataList.observe(viewLifecycleOwner) { data ->
            adapter.data = data
        }

        //Асинхронное обновление данных
        viewModel.loadData()

        //Присваивание адаптера
        binding.friendsRequestsRecycler.adapter = adapter

        binding.backBtn.setOnClickListener {
            findNavController().navigate(FriendsRequestsPageDirections.actionFriendsRequestsPageToFriendsPage())
        }
    }
}