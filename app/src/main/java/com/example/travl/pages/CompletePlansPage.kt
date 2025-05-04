package com.example.travl.pages

import com.example.travl.databinding.CompletePlansPageBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travl.adapters.CompletePlansPageItemAdapter
import com.example.travl.interfaces.OnClickListener
import com.example.travl.viewModels.CompletePlansViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class CompletePlansPage : Fragment(), OnClickListener {
    private lateinit var binding: CompletePlansPageBinding
    private lateinit var adapter: CompletePlansPageItemAdapter
    private val viewModel: CompletePlansViewModel by viewModels()
    private val db = FirebaseFirestore.getInstance()
    private val uid = Firebase.auth.currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CompletePlansPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CompletePlansPageItemAdapter(requireContext(), this)


        //Менеджер RecyclerView
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //Присваивание менеджера
        binding.completePlansRecycler.layoutManager =
            manager


        //Наблюдатель
        viewModel.dataList.observe(viewLifecycleOwner) { data ->
            adapter.data = data
        }

        //Асинхронное обновление данных
        viewModel.loadData()

        //Присваивание адаптера
        binding.completePlansRecycler.adapter = adapter

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onClick(position: Int) {
        val completePlace = adapter.getItem(position)

        if (uid != null) {
            val completeRef = db.collection("users")
                .document(uid)
                .collection("complete")
                .document(completePlace.key)


            completeRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        completeRef.delete()

                        viewModel.removeItem(completePlace.key)

                        showToast("Место удалено из выполненных")
                    } else {
                        showToast("Не удалось удалить место из выполненных")
                    }
                }
                .addOnFailureListener { exception ->
                    showToast("Не удалось найти место в выполненных")
                }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}