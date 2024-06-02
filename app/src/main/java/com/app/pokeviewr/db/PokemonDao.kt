package com.app.pokeviewr.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.app.pokeviewr.model.Pokemon

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun upsert(pokemon: Pokemon): Long

    @Query("SELECT * FROM pokemon")
    fun getAllPokemon(): LiveData<List<Pokemon>>

    @Query("SELECT COUNT(*) FROM Pokemon WHERE species = :species")
    suspend fun getCountBySpecies(species: String): Int

    @Delete
    suspend fun deletePokemon(pokemon: Pokemon)

    @Update
    suspend fun updatePokemon(pokemon: Pokemon)

}