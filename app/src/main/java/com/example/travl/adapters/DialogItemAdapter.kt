package com.example.travl.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.travl.databinding.FriendCardBinding
import com.example.travl.interfaces.OnDialogClickListener
import com.example.travl.items.Friend

class DialogItemAdapter(
    private val context: Context,
    private val listener: OnDialogClickListener
) :
    RecyclerView.Adapter<DialogItemAdapter.DialogItemViewHolder>() {

    var data: List<Friend> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    fun getItem(position: Int): Friend {
        return data[position]
    }

    class DialogItemViewHolder(val binding: FriendCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DialogItemViewHolder {
        val binding =
            FriendCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return DialogItemViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: DialogItemViewHolder, position: Int) {
        val friend = data[position]

        with(holder.binding) {
            friendName.text = friend.friendUsername

            friendCard.setOnClickListener {
                listener.onFriendClicked(position)
            }
        }
    }
}