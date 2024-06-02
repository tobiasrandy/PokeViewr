package com.app.pokeviewr.ui

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.pokeviewr.R
import com.app.pokeviewr.databinding.FragmentPokemonDetailBinding
import com.app.pokeviewr.model.Move
import com.app.pokeviewr.model.PokemonDetailResponse
import com.app.pokeviewr.model.PokemonSpeciesResponse
import com.app.pokeviewr.model.Stat
import com.app.pokeviewr.ui.adapter.MoveAdapter
import com.app.pokeviewr.ui.viewmodel.PokemonDetailViewModel
import com.app.pokeviewr.ui.viewmodelfactory.PokemonViewModelProviderFactory
import com.app.pokeviewr.util.Constants
import com.app.pokeviewr.util.Resource
import com.app.pokeviewr.util.capitalizeFirstChar
import com.app.pokeviewr.util.getTypeDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.chip.Chip
import com.google.android.material.divider.MaterialDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.random.Random


@AndroidEntryPoint
class PokemonDetailFragment : Fragment() {

    private lateinit var binding: FragmentPokemonDetailBinding

    @Inject
    lateinit var viewModelFactory: PokemonViewModelProviderFactory
    private lateinit var viewModel: PokemonDetailViewModel

    private var pokemmonId: Int? = 0
    private var pokemonName: String = ""
    private var palette: Int = 0

    private lateinit var colorStateList: ColorStateList
    private var currentTabIndex = 0

    private lateinit var moveAdapter: MoveAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this, viewModelFactory)[PokemonDetailViewModel::class.java]

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        pokemmonId = arguments?.getInt("id") ?: 0
        pokemonName = arguments?.getString("name") ?: ""
        palette = arguments?.getInt("palette") ?: 0

        binding.pokemonName.text = pokemonName.capitalizeFirstChar()
        binding.pokemonTags.text = String.format("#%04d", pokemmonId)

        if(palette != 0) {
            setupPokemonBackground(palette)
        }

        setupPoster(Constants.IMAGE_URL + pokemmonId + ".png")
        setupTabs()
        setupViewModel()

        return binding.root
    }

    private fun setupPokemonBackground(color: Int) {
        binding.bgPoster.backgroundTintList = ColorStateList.valueOf(color)
        binding.toolbar.setBackgroundColor(color)
    }

    private fun setupPoster(posterUrl: String) {
        val option = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)

        Glide.with(binding.root)
            .load(posterUrl)
            .error(R.drawable.ic_pokeball)
            .apply(option)
            .into(binding.pokemonPoster)

        if(palette == 0) {
            Glide.with(binding.root)
                .asBitmap()
                .load(posterUrl)
                .apply(option)
                .into(object : CustomTarget<Bitmap?>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                        val vibrantSwatch = Palette.from(resource).generate().mutedSwatch
                        palette = vibrantSwatch?.rgb ?: R.color.md_black_1000
                        setupPokemonBackground(palette)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[PokemonDetailViewModel::class.java]
        viewModel.id = pokemmonId!!

        viewModel.pokemonDetailData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    (activity as MainActivity).hideLoadingDialog()
                    response.data?.let { pokemonDetailResponse ->
                        updateView(pokemonDetailResponse)
                    }
                }

                is Resource.Error -> {
                    (activity as MainActivity).hideLoadingDialog()
                    response.message?.let { message ->
                        (activity as MainActivity).showSnackbar(binding.root, getString(R.string.error_alert, message), true)
                    }
                }

                is Resource.Loading -> {
                    (activity as MainActivity).showLoadingDialog()
                }
            }
        }

        viewModel.pokemonSpeciesData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    (activity as MainActivity).hideLoadingDialog()
                    response.data?.let { pokemonSpeciesResponse ->
                        setupSpeciesDetails(pokemonSpeciesResponse)
                    }
                }

                is Resource.Error -> {
                    (activity as MainActivity).hideLoadingDialog()
                    response.message?.let { message ->
                        (activity as MainActivity).showSnackbar(binding.root, getString(R.string.error_alert, message), true)
                    }
                }

                is Resource.Loading -> {

                }
            }
        }
    }

    private fun updateView(response: PokemonDetailResponse) {
        val types = ArrayList<String>()
        types.clear()
        binding.typeChipGroup.removeAllViews()
        for (type in response.types ?: emptyList()) {
            val chip = Chip(requireContext())
            val typeName = type.type?.name?.capitalizeFirstChar() ?: getString(R.string.unknown_type)
            types.add(typeName)
            chip.text = typeName
            chip.chipIcon = ContextCompat.getDrawable(requireContext(), getTypeDrawable(typeName))
            binding.typeChipGroup.addView(chip)
        }

        binding.pokeball.visibility = View.VISIBLE
        binding.pokeball.setOnClickListener {
            if(Random.nextBoolean()) {
                (activity as MainActivity).showSnackbar(binding.root, getString(R.string.pokemon_success), false)
                viewModel.savePokemon(pokemonName, pokemmonId!!, response.abilities!!, types[0], if(types.size > 1) types[1] else "" )

            } else (activity as MainActivity).showSnackbar(binding.root, getString(R.string.pokemon_fail), true)
        }

        binding.tvHeightValue.text = buildString {
            append(response.height?.toDouble()?.div(10))
            append(" m")
        }
        binding.tvWeightValue.text = buildString {
            append(response.weight?.toDouble()?.div(10))
            append(" kg")
        }

        var abilities = ""
        for((index, itemAbility) in response.abilities?.withIndex() ?: emptyList()) {
            abilities += itemAbility.ability?.name?.capitalizeFirstChar()
            if(index != response.abilities?.lastIndex) {
                abilities += ", "
            }
        }
        binding.tvAbilityValue.text = abilities

        setupStats(response.stats!!.toList())
        setupRecyclerView(response.moves!!.toList())
    }

    private fun setupSpeciesDetails(species: PokemonSpeciesResponse) {
        binding.speciesChipGroup.removeAllViews()

        if(species.isBaby!!) {
            val babyChip = Chip(requireContext())
            babyChip.text = getString(R.string.baby)
            binding.speciesChipGroup.addView(babyChip)
        }

        if(species.isLegendary!!) {
            val legendaryChip = Chip(requireContext())
            legendaryChip.text = getString(R.string.legendary)
            binding.speciesChipGroup.addView(legendaryChip)
        }

        if(species.isMythical!!) {
            val mythicalChip = Chip(requireContext())
            mythicalChip.text = getString(R.string.mythical)
            binding.speciesChipGroup.addView(mythicalChip)
        }

        binding.tvAbout.text = (species.flavorTextEntries?.get(0)?.flavorText)?.replace("\n", " ") ?: ""
    }

    private fun setupTabs() {
        colorStateList = binding.tab2.textColors
        binding.tab1.setOnClickListener { v -> initTab(v) }
        binding.tab2.setOnClickListener { v -> initTab(v) }
        binding.tab3.setOnClickListener { v -> initTab(v) }
    }

    private fun setupStats(stats: List<Stat>) {
        for (itemStat in stats) {
            when (itemStat.type?.name?.lowercase()) {
                "hp" -> {
                    binding.statsContainer.barHp.setProgress(itemStat.baseStat!!, true)
                    binding.statsContainer.tvHpValue.text = itemStat.baseStat.toString()
                }
                "attack" -> {
                    binding.statsContainer.barAttack.setProgress(itemStat.baseStat!!, true)
                    binding.statsContainer.tvAttackValue.text = itemStat.baseStat.toString()
                }
                "defense" -> {
                    binding.statsContainer.barDefense.setProgress(itemStat.baseStat!!, true)
                    binding.statsContainer.tvDefenseValue.text = itemStat.baseStat.toString()
                }
                "special-attack" -> {
                    binding.statsContainer.barSpAttack.setProgress(itemStat.baseStat!!, true)
                    binding.statsContainer.tvSpAttackValue.text = itemStat.baseStat.toString()
                }
                "special-defense" -> {
                    binding.statsContainer.barSpDefense.setProgress(itemStat.baseStat!!, true)
                    binding.statsContainer.tvSpDefenseValue.text = itemStat.baseStat.toString()
                }
                else -> {
                    binding.statsContainer.barSpeed.setProgress(itemStat.baseStat!!, true)
                    binding.statsContainer.tvSpeedValue.text = itemStat.baseStat.toString()
                }
            }
        }
    }

    private fun initTab(view: View) {
        when (view.id) {
            binding.tab1.id -> {
                currentTabIndex = 0
                binding.cardViewStats.visibility = View.VISIBLE
                binding.cardViewDetails.visibility = View.GONE
                binding.cardViewMoves.visibility = View.GONE
                binding.select.animate().x(0F).setDuration(100)
                binding.tab1.setTextColor(ContextCompat.getColor(requireContext(), R.color.md_white_1000))
                binding.tab2.setTextColor(colorStateList)
                binding.tab3.setTextColor(colorStateList)
            }

            binding.tab2.id -> {
                currentTabIndex = 1
                binding.cardViewStats.visibility = View.GONE
                binding.cardViewDetails.visibility = View.VISIBLE
                binding.cardViewMoves.visibility = View.GONE
                val size = binding.tab2.width
                binding.select.animate().x(size.toFloat()).setDuration(100)
                binding.tab1.setTextColor(colorStateList)
                binding.tab2.setTextColor(ContextCompat.getColor(requireContext(), R.color.md_white_1000))
                binding.tab3.setTextColor(colorStateList)
            }

            binding.tab3.id -> {
                currentTabIndex = 2
                binding.cardViewStats.visibility = View.GONE
                binding.cardViewDetails.visibility = View.GONE
                binding.cardViewMoves.visibility = View.VISIBLE
                val size = binding.tab2.width * 2
                binding.select.animate().x(size.toFloat()).setDuration(100)
                binding.tab1.setTextColor(colorStateList)
                binding.tab2.setTextColor(colorStateList)
                binding.tab3.setTextColor(ContextCompat.getColor(requireContext(), R.color.md_white_1000))
            }
        }
    }

    private fun setupRecyclerView(moveList: List<Move>) {
        moveAdapter = MoveAdapter()

        val linearLayoutManager = LinearLayoutManager(requireContext())

        binding.rvMoves.apply {
            adapter = moveAdapter
            layoutManager = linearLayoutManager
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(MaterialDividerItemDecoration(requireContext(), linearLayoutManager.orientation).apply {
                isLastItemDecorated = false
            })
        }

        moveAdapter.differ.submitList(moveList)
    }
}