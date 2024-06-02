package com.app.pokeviewr.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.navigation.fragment.findNavController
import com.app.pokeviewr.R
import com.app.pokeviewr.databinding.FragmentHomeBinding
import com.app.pokeviewr.ui.adapter.PokemonAdapter
import com.app.pokeviewr.ui.viewmodel.HomeViewModel
import com.app.pokeviewr.ui.viewmodelfactory.PokemonViewModelProviderFactory
import com.app.pokeviewr.util.EndlessRecyclerViewListener
import com.app.pokeviewr.util.LoadingType
import com.app.pokeviewr.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var viewModelFactory: PokemonViewModelProviderFactory
    private lateinit var viewModel: HomeViewModel

    private lateinit var pokemonAdapter: PokemonAdapter
    private lateinit var scrollListener: EndlessRecyclerViewListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]

        setupRecyclerView()

        viewModel.pokemonData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    showRefreshLoading(false)
                    response.data?.let { pokemonResponse ->
                        pokemonAdapter.differ.submitList(pokemonResponse.results!!.toList())
                    }
                    showEmptyState(pokemonAdapter.itemCount == 0)
                }

                is Resource.Error -> {
                    showRefreshLoading(false)
                    response.message?.let { message ->
                        (activity as MainActivity).showSnackbar(binding.root, getString(R.string.error_alert, message), true)
                    }
                    showEmptyState(pokemonAdapter.itemCount == 0)
                }

                is Resource.Loading -> {
                    when (response.loadingType) {
                        LoadingType.PAGINATION -> {
                            // Handle pagination loading
                        }

                        LoadingType.REFRESH -> {
                            showRefreshLoading(true)
                        }

                        else -> {
                            showRefreshLoading(true)
                        }
                    }
                    showEmptyState(false)
                }
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.clearPokemonList()
            viewModel.getPokemonList()
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        pokemonAdapter = PokemonAdapter()

        val gridLayoutManager = GridLayoutManager(requireContext(), 3)

        scrollListener = object : EndlessRecyclerViewListener(gridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                viewModel.loadingType = LoadingType.PAGINATION
                viewModel.getPokemonList()
            }
        }

        binding.rvPokemon.apply {
            adapter = pokemonAdapter
            layoutManager = gridLayoutManager
            itemAnimator = DefaultItemAnimator()
            addOnScrollListener(scrollListener)
        }

        pokemonAdapter.setOnItemClickListener { pokemonId, pokemonName, palette ->
            val bundle = bundleOf(
                "id" to pokemonId,
                "name" to pokemonName,
                "palette" to palette
            )
            findNavController().navigate(R.id.nav_detail, bundle)
        }
    }

    private fun showRefreshLoading(isLoading: Boolean) {
        binding.swipeRefresh.isRefreshing = isLoading
    }

    private fun showEmptyState(isEmpty: Boolean) {
        binding.tvEmptyPokemon.visibility = if(isEmpty) View.VISIBLE else View.GONE
        binding.rvPokemon.visibility = if(isEmpty) View.GONE else View.VISIBLE
    }
}