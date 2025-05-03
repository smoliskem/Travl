package com.example.travl.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.travl.databinding.JointFriendPageBinding

class JointFriendPage : Fragment() {
    private lateinit var binding: JointFriendPageBinding
    private val args: JointFriendPageArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = JointFriendPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val friendUsername = args.friendUsername

        binding.friendUsername.text = friendUsername

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}