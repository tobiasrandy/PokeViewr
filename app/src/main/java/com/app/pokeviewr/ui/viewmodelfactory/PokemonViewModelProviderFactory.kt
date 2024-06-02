package com.app.pokeviewr.ui.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.pokeviewr.repository.MainRepository
import com.app.pokeviewr.ui.viewmodel.HomeViewModel
import com.app.pokeviewr.ui.viewmodel.MyPokemonViewModel
import com.app.pokeviewr.ui.viewmodel.PokemonDetailViewModel
import javax.inject.Inject

class PokemonViewModelProviderFactory @Inject constructor (
    val app: Application,
    private val mainRepository: MainRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(app, mainRepository) as T
            }
            modelClass.isAssignableFrom(PokemonDetailViewModel::class.java) -> {
                PokemonDetailViewModel(app, mainRepository) as T
            }
            modelClass.isAssignableFrom(MyPokemonViewModel::class.java) -> {
                MyPokemonViewModel(app, mainRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}