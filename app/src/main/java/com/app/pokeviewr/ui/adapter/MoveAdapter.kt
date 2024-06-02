package com.app.pokeviewr.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.pokeviewr.databinding.ItemMoveBinding
import com.app.pokeviewr.model.Move
import com.app.pokeviewr.util.capitalizeFirstChar

class MoveAdapter : RecyclerView.Adapter<MoveAdapter.MoveViewHolder>() {

    inner class MoveViewHolder(val binding: ItemMoveBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Move>() {
        override fun areItemsTheSame(oldItem: Move, newItem: Move): Boolean {
            return oldItem.move?.name == newItem.move?.name
        }

        override fun areContentsTheSame(oldItem: Move, newItem: Move): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoveViewHolder {
        val binding = ItemMoveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoveViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MoveViewHolder, position: Int) {
        val moveItem = differ.currentList[position]
        val binding = holder.binding

        binding.apply {
            tvMove.text = moveItem.move?.name?.capitalizeFirstChar() ?: ""
        }
    }
}