package com.example.travl.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travl.interfaces.OnChildItemClickListener
import com.example.travl.databinding.MainPageItemRecyclerBinding
import com.example.travl.items.MainPageParentItem
import com.example.travl.items.PlaceCard


class MainPageParentItemAdapter(
    private val context: Context,
    private val listener: OnChildItemClickListener
) :
    RecyclerView.Adapter<MainPageParentItemAdapter.MainPageParentItemViewHolder>() {

    var data: List<MainPageParentItem> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    fun getItem(parentPosition: Int, childPosition: Int): PlaceCard {
        return data[parentPosition].childItemList[childPosition]
    }

    class MainPageParentItemViewHolder(val binding: MainPageItemRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root)

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
        with(holder.binding) {
            regionName.text = item.regionName

            mainPageItemRecycler.layoutManager =
                LinearLayoutManager(
                    holder.binding.root.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )

            mainPageItemRecycler.adapter =
                MainPageChildItemAdapter(item.childItemList, context, listener, position)
        }
    }
}