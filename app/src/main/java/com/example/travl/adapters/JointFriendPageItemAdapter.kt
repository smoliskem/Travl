package com.example.travl.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.travl.databinding.MyPlansPlaceCardBinding
import com.example.travl.interfaces.OnMyPlansClickListener
import com.example.travl.items.MyPlansItem


class JointFriendPageItemAdapter(
    private val context: Context,
    private val listener: OnMyPlansClickListener,
) :
    RecyclerView.Adapter<JointFriendPageItemAdapter.JointFriendPageViewHolder>() {

    var data: List<MyPlansItem> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    fun getItem(position: Int): MyPlansItem {
        return data[position]
    }

    class JointFriendPageViewHolder(val binding: MyPlansPlaceCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JointFriendPageViewHolder {
        val binding =
            MyPlansPlaceCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return JointFriendPageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: JointFriendPageViewHolder, position: Int) {
        val item = data[position]

        with(holder.binding) {
            placeName.text = item.placeName
            regionName.text = item.regionName

            Glide.with(context)
                .load(item.imageResURI)
                .into(holder.binding.placeImg)

            deleteBtn.setOnClickListener {
                listener.onDeleteItemClick(position)
            }

            placeImg.setOnClickListener {
                listener.onImageClick(position)
            }

            successBtn.setOnClickListener {
                listener.onAcceptClick(position)
            }
        }
    }
}