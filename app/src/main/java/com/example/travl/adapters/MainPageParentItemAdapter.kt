package com.example.travl.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travl.databinding.MainPageItemRecyclerBinding
import com.example.travl.items.MainPageParentItem


class MainPageParentItemAdapter(private val context: Context) :
    RecyclerView.Adapter<MainPageParentItemAdapter.MainPageParentItemViewHolder>() {

    var data: List<MainPageParentItem> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class MainPageParentItemViewHolder(val binding: MainPageItemRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainPageParentItemViewHolder {
        val binding =
            MainPageItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainPageParentItemViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MainPageParentItemViewHolder, position: Int) {
        val item = data[position]

        holder.binding.regionName.text = item.regionName

        holder.binding.mainPageItemRecycler.layoutManager =
            LinearLayoutManager(holder.binding.root.context, LinearLayoutManager.HORIZONTAL, false)

        holder.binding.mainPageItemRecycler.adapter =
            MainPageChildItemAdapter(item.childItemList, context)
    }
}