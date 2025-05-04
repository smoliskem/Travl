package com.example.travl.pages

import com.example.travl.viewModels.FriendsViewModel
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.travl.adapters.DialogItemAdapter
import com.example.travl.databinding.JointFriendsDialogBinding
import com.example.travl.databinding.PlacePageBinding
import com.example.travl.interfaces.OnDialogClickListener
import com.example.travl.items.MyPlansItem
import com.example.travl.items.toMap
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore


class PlacePage : Fragment(), OnDialogClickListener {
    private val args: PlacePageArgs by navArgs()
    private lateinit var binding: PlacePageBinding
    private val db = FirebaseFirestore.getInstance()
    private val viewModel: FriendsViewModel by viewModels()
    private lateinit var adapter: DialogItemAdapter
    private val uid = Firebase.auth.currentUser?.uid
    private lateinit var favorite: MyPlansItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = PlacePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageResURIArg = args.imageResURL
        val placeNameArg = args.placeName
        val regionNameArg = args.regionName
        val descriptionArg = args.description
        val key = args.key
        favorite = MyPlansItem(
            imageResURIArg,
            placeNameArg,
            regionNameArg,
            descriptionArg,
            key
        )

        Glide.with(requireContext())
            .load(imageResURIArg)
            .centerCrop()
            .into(binding.placeImg)


        with(binding) {
            placeName.text = placeNameArg
            regionName.text = regionNameArg
            placeDescription.text = descriptionArg

            addBtn.setOnClickListener {
                if (uid != null) {
                    onFavorites(
                        db,
                        favorite,
                        uid
                    )
                }
            }

            backBtn.setOnClickListener {
                findNavController().navigate(PlacePageDirections.actionPlacePageToMainPage())
            }

            shareBtn.setOnClickListener {
                showRecyclerDialog()
            }
        }

    }


    private fun showRecyclerDialog() {
        val dialog = Dialog(requireContext()).apply {
            setCancelable(true)
        }

        val dialogBinding = JointFriendsDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialog.window?.apply {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        adapter = DialogItemAdapter(requireContext(), this)

        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        dialogBinding.dialogRecyclerView.layoutManager =
            manager

        viewModel.dataList.observe(viewLifecycleOwner) { data ->
            adapter.data = data
        }

        viewModel.loadData()

        dialogBinding.dialogRecyclerView.adapter = adapter

        dialogBinding.closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun onFavorites(
        db: FirebaseFirestore,
        favorite: MyPlansItem,
        uid: String
    ) {
        val favoritesRef = db.collection("users")
            .document(uid)
            .collection("favorites")
            .document(favorite.key)

        // Проверяем существование документа
        favoritesRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Документ уже существует
                    showToast("Это место уже в избранном")
                } else {
                    // Документа нет - добавляем
                    favoritesRef.set(favorite.toMap())
                        .addOnSuccessListener {
                            showToast("Место добавлено в избранное")
                        }
                        .addOnFailureListener { e ->
                            showToast("Ошибка добавления")
                        }
                }
            }
            .addOnFailureListener { e ->
                showToast("Ошибка проверки избранного")
            }
    }

    override fun onFriendClicked(position: Int) {
        val friend = adapter.getItem(position)
        if (uid == null) return

        val currentUserPlanRef = db.collection("users")
            .document(uid)
            .collection("friends")
            .document(friend.friendUserID)
            .collection("jointPlans")
            .document(favorite.key)

        currentUserPlanRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                showToast("Этот план уже добавлен")
            }
            else {
                val planData = favorite.toMap()

                currentUserPlanRef.set(planData)
                db.collection("users")
                    .document(friend.friendUserID)
                    .collection("friends")
                    .document(uid)
                    .collection("jointPlans")
                    .document(favorite.key)
                    .set(planData)
                    .addOnCompleteListener {
                        showToast("Совместный план добавлен с ${friend.friendUsername}")
                    }
            }
        }.addOnFailureListener {
            showToast("Ошибка при проверке плана")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}