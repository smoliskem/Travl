package com.example.travl.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travl.adapters.JointFriendPageItemAdapter
import com.example.travl.databinding.JointFriendPageBinding
import com.example.travl.interfaces.OnMyPlansClickListener
import com.example.travl.items.toMap
import com.example.travl.viewModels.JointFriendViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class JointFriendPage : Fragment(), OnMyPlansClickListener {
    private lateinit var binding: JointFriendPageBinding
    private lateinit var adapter: JointFriendPageItemAdapter
    private val viewModel: JointFriendViewModel by viewModels()
    private val args: JointFriendPageArgs by navArgs()
    private val db = FirebaseFirestore.getInstance()
    private val uid = Firebase.auth.currentUser?.uid
    private lateinit var friendUsername: String
    private lateinit var friendUserID: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = JointFriendPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        friendUsername = args.friendUsername
        friendUserID = args.friendUserID

        viewModel.setSelectedFriendId(friendUserID)

        binding.friendUsername.text = friendUsername

        adapter = JointFriendPageItemAdapter(requireContext(), this)


        //Менеджер RecyclerView
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //Присваивание менеджера
        binding.jointFriendRecycler.layoutManager =
            manager


        //Наблюдатель
        viewModel.dataList.observe(viewLifecycleOwner) { data ->
            adapter.data = data
        }

        //Асинхронное обновление данных
        viewModel.loadData()

        //Присваивание адаптера
        binding.jointFriendRecycler.adapter = adapter

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDeleteItemClick(position: Int) {
        val item = adapter.getItem(position)

        if (uid != null) {
            val docRef = db.collection("users")
                .document(uid)
                .collection("friends")
                .document(friendUserID)
                .collection("jointPlans")
                .document(item.key)

            val friendDocRef = db.collection("users")
                .document(friendUserID)
                .collection("friends")
                .document(uid)
                .collection("jointPlans")
                .document(item.key)

            docRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        docRef.delete()
                            .addOnSuccessListener {
                                friendDocRef.get()
                                    .addOnSuccessListener { friendDocument ->
                                        if (friendDocument.exists()) {
                                            friendDocRef.delete()

                                            viewModel.removeItem(item.key)

                                            showToast("Удалено из Совместных планов с ${friendUsername}")
                                        }
                                    }
                            }
                            .addOnFailureListener { e ->
                                showToast("Ошибка удаления: ${e.message}")
                            }
                    } else {
                        showToast("Элемент не найден в избранном, обновите страницу")
                    }
                }
                .addOnFailureListener { e ->
                    showToast("Ошибка проверки: ${e.message}")
                }
        }
    }

    override fun onImageClick(position: Int) {
        val childItem = adapter.getItem(position)

        val action = JointFriendPageDirections.actionJointFriendPageToPlacePage(
            childItem.imageResURI,
            childItem.placeName,
            childItem.regionName,
            childItem.description,
            childItem.key
        )

        findNavController().navigate(action)
    }

    override fun onAcceptClick(position: Int) {
        val place = adapter.getItem(position)
        if (uid == null) return


        val completeCurrRef = db.collection("users")
            .document(uid)
            .collection("complete")
            .document(place.key)

        val completeFriendRef = db.collection("users")
            .document(friendUserID)
            .collection("complete")
            .document(place.key)

        val placeCurrRef = db.collection("users")
            .document(uid)
            .collection("favorites")
            .document(place.key)

        val placeFriendRef = db.collection("users")
            .document(friendUserID)
            .collection("favorites")
            .document(place.key)

        completeCurrRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    showToast("Это место уже добавлено в выполненные")
                } else {
                    completeCurrRef.set(place.toMap())
                        .addOnSuccessListener {
                            completeFriendRef.set(place.toMap())
                                .addOnSuccessListener {
                                    placeCurrRef.delete()
                                    placeFriendRef.delete()
                                    viewModel.removeItem(place.key)
                                    showToast("Место добавлено в выполненные")
                                }
                        }
                        .addOnFailureListener { e ->
                            showToast("Ошибка добавления")
                        }
                }
            }
            .addOnFailureListener { e ->
                showToast("Ошибка проверки выполненных")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}