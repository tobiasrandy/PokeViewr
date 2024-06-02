package com.app.pokeviewr.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.app.pokeviewr.model.Pokemon
import com.app.pokeviewr.repository.MainRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class MyPokemonViewModel @Inject constructor(app: Application, private val repository: MainRepository) : AndroidViewModel(app) {

    fun getCaughtPokemon() = repository.getAllPokemon()

    fun releasePokemon(pokemon: Pokemon) = viewModelScope.launch {
        repository.deletePokemon(pokemon)
    }

    fun savePokemon(pokemon: Pokemon) = viewModelScope.launch {
        repository.upsert(pokemon)
    }

    fun updatePokemon(pokemon: Pokemon) = viewModelScope.launch {
        repository.updatePokemon(pokemon)
    }
}