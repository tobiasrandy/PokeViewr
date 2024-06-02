package com.app.pokeviewr.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonDetailResponse(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("height")
    val height: Int? = null,

    @field:SerializedName("weight")
    val weight: Int? = null,

    @field:SerializedName("abilities")
    val abilities: List<Ability>? = null,

    @field:SerializedName("moves")
    val moves: List<Move>? = null,

    @field:SerializedName("types")
    val types: List<Type>? = null,

    @field:SerializedName("stats")
    val stats: List<Stat>? = null,
) : Parcelable

@Parcelize
data class Ability(
    @field:SerializedName("is_hidden")
    val isHidden: Boolean? = null,

    @field:SerializedName("slot")
    val slot: Int? = null,

    @field:SerializedName("ability")
    val ability: NamedResource? = null
) : Parcelable

@Parcelize
data class Move(
    @field:SerializedName("move")
    val move: NamedResource? = null
) : Parcelable

@Parcelize
data class Type(
    @field:SerializedName("slot")
    val slot: Int? = null,

    @field:SerializedName("type")
    val type: NamedResource? = null
) : Parcelable

@Parcelize
data class Stat(
    @field:SerializedName("base_stat")
    val baseStat: Int? = null,

    @field:SerializedName("stat")
    val type: NamedResource? = null
) : Parcelable