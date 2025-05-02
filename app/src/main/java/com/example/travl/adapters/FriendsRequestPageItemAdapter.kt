package com.example.travl.adapters

import com.example.travl.databinding.RequestCardBinding
import com.example.travl.items.FriendRequest
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.travl.interfaces.OnFriendRequestClickListener
import com.example.travl.items.MyPlansItem

class FriendsRequestPageItemAdapter(
    private val context: Context,
    private val listener: OnFriendRequestClickListener
) :
    RecyclerView.Adapter<FriendsRequestPageItemAdapter.FriendsRequestPageItemViewHolder>() {

    var data: List<FriendRequest> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }


    fun getItem(position: Int): FriendRequest {
        return data[position]
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
            friendName.text = friendRequest.fromUsername

            acceptBtn.setOnClickListener {
                listener.onAcceptClick(position)
            }

            rejectBtn.setOnClickListener {
                listener.onRejectClick(position)
            }
        }
    }
}