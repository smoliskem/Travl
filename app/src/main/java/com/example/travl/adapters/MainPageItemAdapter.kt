package com.example.travl.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.travl.databinding.MainPlaceCardBinding
import com.example.travl.items.MainPageItem

class MainPageItemAdapter : RecyclerView.Adapter<MainPageItemAdapter.MainPageItemViewHolder>() {

    var data: List<MainPageItem> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class MainPageItemViewHolder(val binding: MainPlaceCardBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainPageItemViewHolder {
        val binding =
            MainPlaceCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MainPageItemViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MainPageItemViewHolder, position: Int) {
        val place = data[position]

        with(holder.binding) {
            placeName.text = place.placeName
            regionName.text = place.regionName
            score.text = place.score
            frame.setBackgroundResource(place.imageResId)
        }
    }
}