package com.app.pokeviewr.ui

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.pokeviewr.R
import com.app.pokeviewr.databinding.DialogEditNameBinding
import com.app.pokeviewr.databinding.FragmentMyPokemonBinding
import com.app.pokeviewr.ui.adapter.MyPokemonAdapter
import com.app.pokeviewr.ui.viewmodel.MyPokemonViewModel
import com.app.pokeviewr.ui.viewmodelfactory.PokemonViewModelProviderFactory
import com.app.pokeviewr.util.isPrime
import com.app.pokeviewr.util.validateName
import com.app.pokeviewr.util.validateText
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import javax.inject.Inject


@AndroidEntryPoint
class MyPokemonFragment : Fragment() {

    private lateinit var binding: FragmentMyPokemonBinding

    @Inject
    lateinit var viewModelFactory: PokemonViewModelProviderFactory
    private lateinit var viewModel: MyPokemonViewModel

    private lateinit var myPokemonAdapter: MyPokemonAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMyPokemonBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this, viewModelFactory)[MyPokemonViewModel::class.java]

        setupRecyclerView()

        viewModel.getCaughtPokemon().observe(viewLifecycleOwner) { pokemonList ->
            myPokemonAdapter.differ.submitList(pokemonList)
            showEmptyState(pokemonList.isEmpty())
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        myPokemonAdapter = MyPokemonAdapter()

        binding.rvPokemon.apply {
            adapter = myPokemonAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.ACTION_STATE_IDLE,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return true
            }

            @SuppressLint("StringFormatMatches")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val pokemon = myPokemonAdapter.differ.currentList[position]

                when(direction) {
                    ItemTouchHelper.LEFT -> {
                        val randomNumber = (0..50).random()

                        viewModel.releasePokemon(pokemon)

                        if(isPrime(randomNumber)) {
                            (activity as MainActivity).showSnackbar(binding.root, getString(R.string.release_pokemon_success, pokemon.name), false)
                        } else {
                            (activity as MainActivity).showSnackbar(binding.root, getString(R.string.release_pokemon_fail, randomNumber), true)
                            viewModel.savePokemon(pokemon)
                            myPokemonAdapter.notifyItemChanged(position)
                        }
                    }
                    ItemTouchHelper.RIGHT -> {
                        val dialogEditNameBinding = DialogEditNameBinding.inflate(LayoutInflater.from(context))
                        dialogEditNameBinding.editTextName.setText(pokemon.name)
                        dialogEditNameBinding.editTextName.validateText(
                            { text -> validateName(text) },
                            getString(R.string.empty_name_validation),
                            dialogEditNameBinding.inputLayoutName
                        )
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle(getString(R.string.edit_name_title))
                            .setView(dialogEditNameBinding.root)
                            .setNegativeButton("Cancel") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .setPositiveButton("Confirm") { dialog, _ ->
                                pokemon.name = dialogEditNameBinding.editTextName.text.toString().trim()
                                viewModel.updatePokemon(pokemon)
                                dialog.dismiss()
                            }
                            .setOnDismissListener {
                                myPokemonAdapter.notifyItemChanged(position)
                            }
                            .show()
                    }
                }


            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftActionIcon(R.drawable.ic_trash)
                    .addSwipeRightActionIcon(R.drawable.ic_edit)
                    .create()
                    .decorate()

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvPokemon)
        }
    }

    private fun showEmptyState(isEmpty: Boolean) {
        binding.tvEmptyPokemon.visibility = if(isEmpty) View.VISIBLE else View.GONE
        binding.rvPokemon.visibility = if(isEmpty) View.GONE else View.VISIBLE
    }
}