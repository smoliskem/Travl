package com.example.travl.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.travl.databinding.FriendCardBinding
import com.example.travl.items.Friend

class FriendsPageItemAdapter(
    private val context: Context,
) :
    RecyclerView.Adapter<FriendsPageItemAdapter.FriendsPageItemViewHolder>() {

    var data: List<Friend> =  emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class FriendsPageItemViewHolder(val binding: FriendCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FriendsPageItemViewHolder {
        val binding =
            FriendCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return FriendsPageItemViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: FriendsPageItemViewHolder, position: Int) {
        val friend = data[position]

        with(holder.binding) {
            friendName.text = friend.username
        }
    }
}