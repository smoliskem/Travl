package com.example.travl.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.travl.databinding.MainPlaceCardBinding
import com.example.travl.items.MainPageChildItem

class MainPageChildItemAdapter(private val innerData: List<MainPageChildItem>) :
    RecyclerView.Adapter<MainPageChildItemAdapter.MainPageChildItemViewHolder>() {

    public var data: List<MainPageChildItem> = innerData
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class MainPageChildItemViewHolder(val binding: MainPlaceCardBinding) :
        RecyclerView.ViewHolder(binding.root) {}

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
            score.text = place.score
            frame.setBackgroundResource(place.imageResId)
        }
    }
}