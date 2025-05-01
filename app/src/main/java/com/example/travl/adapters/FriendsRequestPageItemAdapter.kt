package com.example.travl.adapters

import com.example.travl.databinding.RequestCardBinding
import com.example.travl.items.FriendRequest
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FriendsRequestPageItem(
    innerData: List<FriendRequest>,
    private val context: Context,
    private val parentPosition: Int
) :
    RecyclerView.Adapter<FriendsRequestPageItem.FriendsRequestPageItemViewHolder>() {

    var data: List<FriendRequest> = innerData
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class FriendsRequestPageItemViewHolder(val binding: RequestCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FriendsRequestPageItemViewHolder {
        val binding =
            RequestCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return FriendsRequestPageItemViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: FriendsRequestPageItemViewHolder, position: Int) {
        val friendRequest = data[position]

        with(holder.binding) {
            friendName.text = friendRequest.username
        }
    }
}