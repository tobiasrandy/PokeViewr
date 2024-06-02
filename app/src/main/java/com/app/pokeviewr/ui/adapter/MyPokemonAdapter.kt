package com.app.pokeviewr.ui.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.pokeviewr.R
import com.app.pokeviewr.databinding.ItemMyPokemonBinding
import com.app.pokeviewr.model.Pokemon
import com.app.pokeviewr.util.capitalizeFirstChar
import com.app.pokeviewr.util.getTypeDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.chip.Chip

class MyPokemonAdapter : RecyclerView.Adapter<MyPokemonAdapter.MyPokemonViewHolder>() {

    inner class MyPokemonViewHolder(val binding: ItemMyPokemonBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Pokemon>() {
        override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPokemonViewHolder {
        val binding = ItemMyPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyPokemonViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MyPokemonViewHolder, position: Int) {
        val pokemon = differ.currentList[position]
        val binding = holder.binding

        binding.apply {

            val option = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)

            Glide.with(binding.root)
                .load(pokemon.image)
                .placeholder(R.drawable.ic_pokeball)
                .error(R.drawable.ic_pokeball)
                .apply(option)
                .into(pokemonPoster)

            Glide.with(binding.root)
                .asBitmap()
                .load(pokemon.image)
                .apply(option)
                .into(object : CustomTarget<Bitmap?>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                        val vibrantSwatch = Palette.from(resource).generate().mutedSwatch
                        cardView.setCardBackgroundColor(vibrantSwatch?.rgb ?: R.color.md_black_1000)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })

            tvPokemonName.text = pokemon.name
            tvAbility.text = pokemon.ability
            binding.typeChipGroup.removeAllViews()

            val chip1 = Chip(binding.root.context)
            val type1 = pokemon.type1
            chip1.text = type1
            chip1.chipIcon = ContextCompat.getDrawable(binding.root.context, getTypeDrawable(type1))
            chip1.isClickable = false
            typeChipGroup.addView(chip1)

            if(pokemon.type2.isNotEmpty()) {
                val chip2 = Chip(binding.root.context)
                val type2 = pokemon.type2
                chip2.text = type2
                chip2.chipIcon = ContextCompat.getDrawable(binding.root.context, getTypeDrawable(type2))
                chip2.isClickable = false
                typeChipGroup.addView(chip2)
            }

            shinyLogo.visibility = if (pokemon.isShiny) View.VISIBLE else View.GONE
        }
    }
}