package com.app.pokeviewr.repository

import com.app.pokeviewr.db.PokemonDatabase
import com.app.pokeviewr.model.Pokemon
import com.app.pokeviewr.network.ApiService
import javax.inject.Inject

class MainRepository @Inject constructor (private val apiService: ApiService, private val pokemonDatabase: PokemonDatabase) {

    suspend fun getPokemonList(limit: Int, offset: Int) = apiService.getPokemonList(limit, offset)

    suspend fun getPokemonDetail(id: Int) = apiService.getPokemonDetail(id)

    suspend fun getPokemonSpecies(id: Int) = apiService.getPokemonSpecies(id)

    suspend fun upsert(pokemon: Pokemon) = pokemonDatabase.getPokemonDao().upsert(pokemon)

    fun getAllPokemon() = pokemonDatabase.getPokemonDao().getAllPokemon()

    suspend fun getCountBySpecies(species: String) : Int = pokemonDatabase.getPokemonDao().getCountBySpecies(species)

    suspend fun deletePokemon(pokemon: Pokemon) = pokemonDatabase.getPokemonDao().deletePokemon(pokemon)

    suspend fun updatePokemon(pokemon: Pokemon) = pokemonDatabase.getPokemonDao().updatePokemon(pokemon)

}