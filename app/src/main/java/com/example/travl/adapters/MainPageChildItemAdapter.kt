package com.example.travl.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.travl.interfaces.OnChildItemClickListener
import com.example.travl.databinding.MainPlaceCardBinding
import com.example.travl.items.PlaceCard

class MainPageChildItemAdapter(
    innerData: List<PlaceCard>,
    private val context: Context,
    private val listener: OnChildItemClickListener // Добавляем слушатель
) :
    RecyclerView.Adapter<MainPageChildItemAdapter.MainPageChildItemViewHolder>() {

    var data: List<PlaceCard> = innerData
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class MainPageChildItemViewHolder(val binding: MainPlaceCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainPageChildItemViewHolder {
        val binding =
            MainPlaceCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MainPageChildItemViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MainPageChildItemViewHolder, position: Int) {
        val place = data[position]

        with(holder.binding) {
            placeName.text = place.placeName
            regionName.text = place.regionName
            score.text = "4.5"
            Glide.with(context)
                .load(place.imageResURL)
                .into(holder.binding.frame)

            root.setOnClickListener {
                listener.onChildItemClick(position) // Передаем позицию элемента
            }
        }
    }
}