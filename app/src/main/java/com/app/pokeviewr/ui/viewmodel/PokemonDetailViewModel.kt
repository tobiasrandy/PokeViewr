package com.app.pokeviewr.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.pokeviewr.model.Ability
import com.app.pokeviewr.model.Pokemon
import com.app.pokeviewr.model.PokemonDetailResponse
import com.app.pokeviewr.model.PokemonSpeciesResponse
import com.app.pokeviewr.repository.MainRepository
import com.app.pokeviewr.util.Constants
import com.app.pokeviewr.util.LoadingType
import com.app.pokeviewr.util.NetworkManager
import com.app.pokeviewr.util.Resource
import com.app.pokeviewr.util.capitalizeFirstChar
import com.app.pokeviewr.util.isShinyPokemon
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class PokemonDetailViewModel @Inject constructor(app: Application, private val repository: MainRepository) : AndroidViewModel(app) {

    val pokemonDetailData: MutableLiveData<Resource<PokemonDetailResponse>> = MutableLiveData()
    val pokemonSpeciesData: MutableLiveData<Resource<PokemonSpeciesResponse>> = MutableLiveData()

    private var loadingType: LoadingType = LoadingType.INITIAL

    var id: Int = 0
        set(value) {
            field = value
            getDetail()
            getSpecies()
        }

    private fun getDetail() {
        viewModelScope.launch {
            safePokemonDetailCall()
        }
    }

    private fun getSpecies() {
        viewModelScope.launch {
            safePokemonSpeciesCall()
        }
    }

    private suspend fun safePokemonDetailCall() {
        pokemonDetailData.postValue(Resource.Loading(type = loadingType))
        try {
            if(NetworkManager(getApplication()).isNetworkAvailable()) {
                val response = repository.getPokemonDetail(id)
                pokemonDetailData.postValue(handlePokemonDetailResponse(response))
            } else {
                pokemonDetailData.postValue(Resource.Error("No internet connection"))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> pokemonDetailData.postValue(Resource.Error("Network Failure"))
                else -> pokemonDetailData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safePokemonSpeciesCall() {
        try {
            if(NetworkManager(getApplication()).isNetworkAvailable()) {
                val response = repository.getPokemonSpecies(id)
                pokemonSpeciesData.postValue(handlePokemonSpeciesResponse(response))
            } else {
                pokemonSpeciesData.postValue(Resource.Error("No internet connection"))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> pokemonSpeciesData.postValue(Resource.Error("Network Failure"))
                else -> pokemonSpeciesData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handlePokemonDetailResponse(response: Response<PokemonDetailResponse>) : Resource<PokemonDetailResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handlePokemonSpeciesResponse(response: Response<PokemonSpeciesResponse>) : Resource<PokemonSpeciesResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun fibonacci(index: Int): Int {
        var a = 0
        var b = 1
        var result = 0

        if (index == 0) return a
        if (index == 1) return b

        for (i in 2..index) {
            result = a + b
            a = b
            b = result
        }

        return result
    }

    fun savePokemon(pokemonName: String, pokemonId: Int, abilities: List<Ability>, type1: String, type2: String) = viewModelScope.launch {
        val isShiny = isShinyPokemon()
        val imageUrl = Constants.IMAGE_URL + (if (isShiny) "/shiny/" else "") + pokemonId + ".png"
        val ability = if (abilities.size == 1) {
            abilities[0].ability?.name
        } else {
            val randomIndex = abilities.indices.random()
            abilities[randomIndex].ability?.name
        }
        val pokemon = Pokemon(
            name = "Mighty ${pokemonName.capitalizeFirstChar()}-${fibonacci(repository.getCountBySpecies(pokemonName))}",
            species = pokemonName,
            isShiny = isShiny,
            ability = ability!!.capitalizeFirstChar(),
            image = imageUrl,
            type1 = type1,
            type2 = type2
        )
        repository.upsert(pokemon)
    }
}