package com.example.travl.pages

import FriendsRequestsViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travl.adapters.FriendsRequestPageItemAdapter
import com.example.travl.databinding.FriendsRequestPageBinding
import com.example.travl.interfaces.OnFriendRequestClickListener
import com.example.travl.items.Friend
import com.example.travl.items.toMap
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class FriendsRequestsPage : Fragment(), OnFriendRequestClickListener {
    private lateinit var binding: FriendsRequestPageBinding
    private val viewModel: FriendsRequestsViewModel by viewModels()
    private lateinit var adapter: FriendsRequestPageItemAdapter
    private val db = FirebaseFirestore.getInstance()
    private val uid = Firebase.auth.currentUser?.uid
    private val currName = Firebase.auth.currentUser?.displayName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FriendsRequestPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FriendsRequestPageItemAdapter(requireContext(), this)

        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.friendsRequestsRecycler.layoutManager =
            manager

        viewModel.dataList.observe(viewLifecycleOwner) { data ->
            adapter.data = data
        }

        viewModel.loadData()

        binding.friendsRequestsRecycler.adapter = adapter

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onAcceptClick(position: Int) {
        val item = adapter.getItem(position)

        if (uid != null) {
            val docRef = db.collection("users")
                .document(uid)
                .collection("friendRequests")
                .document(item.fromUserID)

            docRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        docRef.delete()
                            .addOnSuccessListener {
                                viewModel.removeItem(item.fromUserID)
                                val friend = Friend(item.fromUserID, item.fromUsername)
                                if (currName != null) {
                                    val curr = Friend(uid, currName)

                                    db.collection("users")
                                        .document(uid)
                                        .collection("friends")
                                        .document(item.fromUserID)
                                        .set(friend.toMap())


                                    db.collection("users")
                                        .document(item.fromUserID)
                                        .collection("friends")
                                        .document(uid)
                                        .set(curr.toMap())

                                    showToast("Заявка принята")
                                }
                            }
                            .addOnFailureListener { e ->
                                showToast("Ошибка удаления: ${e.message}")
                            }
                    } else {
                        showToast("Заявка не найдена")
                    }
                }
                .addOnFailureListener { e ->
                    showToast("Ошибка проверки: ${e.message}")
                }
        }
    }

    override fun onRejectClick(position: Int) {
        val item = adapter.getItem(position)

        if (uid != null) {
            val docRef = db.collection("users")
                .document(uid)
                .collection("friendRequests")
                .document(item.fromUserID)

            docRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        docRef.delete()
                            .addOnSuccessListener {
                                viewModel.removeItem(item.fromUserID)

                                showToast("Заявка отклонена")
                            }
                            .addOnFailureListener { e ->
                                showToast("Ошибка отклонения: ${e.message}")
                            }
                    } else {
                        showToast("Заявка не найдена")
                    }
                }
                .addOnFailureListener { e ->
                    showToast("Ошибка проверки: ${e.message}")
                }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}