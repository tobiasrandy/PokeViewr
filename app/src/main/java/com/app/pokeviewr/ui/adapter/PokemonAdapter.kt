package com.app.pokeviewr.ui.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.pokeviewr.R
import com.app.pokeviewr.databinding.ItemPokemonBinding
import com.app.pokeviewr.model.NamedResource
import com.app.pokeviewr.util.Constants
import com.app.pokeviewr.util.capitalizeFirstChar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class PokemonAdapter : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    inner class PokemonViewHolder(val binding: ItemPokemonBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<NamedResource>() {
        override fun areItemsTheSame(oldItem: NamedResource, newItem: NamedResource): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: NamedResource, newItem: NamedResource): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Int, String, Int) -> Unit)? = null

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = differ.currentList[position]
        val binding = holder.binding

        binding.apply {
            val id = extractPokemonId(pokemon.url!!)
            val posterUrl = Constants.IMAGE_URL + id + ".png"
            val option = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
            var palette = 0

            Glide.with(binding.root)
                .load(posterUrl)
                .placeholder(R.drawable.ic_pokeball)
                .error(R.drawable.ic_pokeball)
                .apply(option)
                .into(pokemonPoster)

            Glide.with(binding.root)
                .asBitmap()
                .load(posterUrl)
                .apply(option)
                .into(object : CustomTarget<Bitmap?>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {

                        val vibrantSwatch = Palette.from(resource).generate().mutedSwatch
                        cardView.setCardBackgroundColor(vibrantSwatch?.rgb ?: R.color.md_black_1000)
                        palette = vibrantSwatch?.rgb ?: R.color.md_black_1000
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })

            tvPokemonName.text = pokemon.name!!.capitalizeFirstChar()

            root.setOnClickListener {
                onItemClickListener?.let { it(id ?: -1, pokemon.name, palette) }
            }
        }
    }

    private fun extractPokemonId(url: String): Int? {
        val parts = url.split("/")
        val idString = parts[parts.size - 2]
        return idString.toIntOrNull()
    }

    fun setOnItemClickListener(listener: (Int, String, Int) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}