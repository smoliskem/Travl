package com.example.travl.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travl.adapters.MyPlansPageItemAdapter
import com.example.travl.databinding.MyPlansPageBinding
import com.example.travl.interfaces.OnMyPlansClickListener
import com.example.travl.items.toMap
import com.example.travl.viewModels.MyPlansPageViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore


class MyPlansPage : Fragment(), OnMyPlansClickListener {
    private lateinit var binding: MyPlansPageBinding
    private lateinit var adapter: MyPlansPageItemAdapter
    private val viewModel: MyPlansPageViewModel by viewModels()
    private val db = FirebaseFirestore.getInstance()
    private val uid = Firebase.auth.currentUser?.uid


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = MyPlansPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Инициализация адаптера
        adapter = MyPlansPageItemAdapter(requireContext(), this)


        //Менеджер RecyclerView
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //Присваивание менеджера
        binding.myPlansRecycler.layoutManager =
            manager


        //Наблюдатель
        viewModel.dataList.observe(viewLifecycleOwner) { data ->
            adapter.data = data
        }

        //Асинхронное обновление данных
        viewModel.loadData()

        //Присваивание адаптера
        binding.myPlansRecycler.adapter = adapter


        //Навигация
        binding.mainPageBtn.setOnClickListener {
            findNavController().navigate(MyPlansPageDirections.actionMyPlansPageToMainPage())
        }
        binding.jointPlansBtn.setOnClickListener {
            findNavController().navigate(MyPlansPageDirections.actionMyPlansPageToJointPlansPage())
        }
        binding.profileBtn.setOnClickListener {
            findNavController().navigate(MyPlansPageDirections.actionMyPlansPageToProfilePage())
        }
    }

    override fun onDeleteItemClick(position: Int) {
        val item = adapter.getItem(position)

        if (uid != null) {
            val docRef = db.collection("users")
                .document(uid)
                .collection("favorites")
                .document(item.key)

            docRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        docRef.delete()
                            .addOnSuccessListener {
                                viewModel.removeItem(item.key)

                                showToast("Удалено из избранного")
                            }
                            .addOnFailureListener { e ->
                                showToast("Ошибка удаления: ${e.message}")
                            }
                    } else {
                        showToast("Элемент не найден в избранном")
                    }
                }
                .addOnFailureListener { e ->
                    showToast("Ошибка проверки: ${e.message}")
                }
        }
    }

    override fun onImageClick(position: Int) {
        val childItem = adapter.getItem(position)

        val action = MyPlansPageDirections.actionMyPlansPageToPlacePage(
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


        val completeRef = db.collection("users")
            .document(uid)
            .collection("complete")
            .document(place.key)

        val placeRef = db.collection("users")
            .document(uid)
            .collection("favorites")
            .document(place.key)

        completeRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    showToast("Это место уже добавлено в выполненные")
                } else {
                    completeRef.set(place.toMap())
                        .addOnSuccessListener {
                            placeRef.delete()
                            viewModel.removeItem(place.key)
                            showToast("Место добавлено в выполненные")
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

