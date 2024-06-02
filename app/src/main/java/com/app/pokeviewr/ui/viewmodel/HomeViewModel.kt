package com.app.pokeviewr.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.pokeviewr.model.PokemonListResponse
import com.app.pokeviewr.repository.MainRepository
import com.app.pokeviewr.util.LoadingType
import com.app.pokeviewr.util.NetworkManager
import com.app.pokeviewr.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class HomeViewModel @Inject constructor(app: Application, private val repository: MainRepository) : AndroidViewModel(app) {

    val pokemonData: MutableLiveData<Resource<PokemonListResponse>> = MutableLiveData()
    private var pokemonListResponse: PokemonListResponse? = null
    private var offset = 0
    var loadingType: LoadingType = LoadingType.INITIAL

    init {
        getPokemonList()
    }

    fun getPokemonList() = viewModelScope.launch {
        safePokemonListCall()
    }

    fun clearPokemonList() {
        loadingType = LoadingType.INITIAL
        offset = 0
        pokemonListResponse = null
    }

    private suspend fun safePokemonListCall() {
        pokemonData.postValue(Resource.Loading(type = loadingType))
        try {
            if(NetworkManager(getApplication()).isNetworkAvailable()) {
                val response = repository.getPokemonList(limit, offset)
                pokemonData.postValue(handlePokemonResponse(response))
            } else {
                pokemonData.postValue(Resource.Error("No internet connection"))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> pokemonData.postValue(Resource.Error("Network Failure"))
                else -> pokemonData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handlePokemonResponse(response: Response<PokemonListResponse>) : Resource<PokemonListResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                offset += limit
                if(pokemonListResponse == null) {
                    pokemonListResponse = resultResponse
                } else {
                    val oldPokemons = pokemonListResponse?.results
                    val newPokemons = resultResponse.results
                    oldPokemons?.addAll(newPokemons!!)
                }
                return Resource.Success(pokemonListResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    companion object {
        private const val limit = 18
    }
}