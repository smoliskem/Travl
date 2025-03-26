package com.example.travl.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.travl.databinding.MyPlansPlaceCardBinding
import com.example.travl.items.PlaceCard

class MyPlansPageItemAdapter(
    private val context: Context
) :
    RecyclerView.Adapter<MyPlansPageItemAdapter.MyPlansPageViewHolder>() {

    var data: List<PlaceCard> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class MyPlansPageViewHolder(val binding: MyPlansPlaceCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPlansPageViewHolder {
        val binding =
            MyPlansPlaceCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyPlansPageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyPlansPageViewHolder, position: Int) {
        val item = data[position]

        with(holder.binding) {
            placeName.text = item.placeName
            regionName.text = item.regionName

            Glide.with(context)
                .load(item.imageResURL)
                .into(holder.binding.placeImg)
        }
    }
}