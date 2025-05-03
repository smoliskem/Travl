package com.example.travl.pages

import FriendsViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travl.adapters.JointPlansPageItemAdapter
import com.example.travl.databinding.JointPlansPageBinding
import com.example.travl.interfaces.OnJointPlansClickListener


class JointPlansPage : Fragment(), OnJointPlansClickListener {
    private val viewModel: FriendsViewModel by viewModels()
    private lateinit var adapter: JointPlansPageItemAdapter
    private lateinit var binding: JointPlansPageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = JointPlansPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = JointPlansPageItemAdapter(requireContext(), this)

        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.jointPlansRecycler.layoutManager =
            manager

        viewModel.dataList.observe(viewLifecycleOwner) { data ->
            adapter.data = data
        }

        viewModel.loadData()

        binding.jointPlansRecycler.adapter = adapter

        binding.mainPageBtn.setOnClickListener {
            findNavController().navigate(JointPlansPageDirections.actionJointPlansPageToMainPage())
        }
        binding.profileBtn.setOnClickListener {
            findNavController().navigate(JointPlansPageDirections.actionJointPlansPageToProfilePage())
        }

        binding.myPlansBtn.setOnClickListener {
            findNavController().navigate(JointPlansPageDirections.actionJointPlansPageToMyPlansPage())
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(position: Int) {
        val friendUsername = adapter.getItem(position).friendUsername

        findNavController().navigate(
            JointPlansPageDirections.actionJointPlansPageToJointFriendPlans(
                friendUsername
            )
        )

    }
}