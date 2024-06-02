package com.app.pokeviewr.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonSpeciesResponse(
    @SerializedName("is_baby")
    val isBaby: Boolean? = null,

    @SerializedName("is_legendary")
    val isLegendary: Boolean? = null,

    @SerializedName("is_mythical")
    val isMythical: Boolean? = null,

    @SerializedName("flavor_text_entries")
    val flavorTextEntries: List<FlavorTextEntry>? = null
) : Parcelable

@Parcelize
data class FlavorTextEntry(
    @SerializedName("flavor_text")
    val flavorText: String? = null,

    @SerializedName("language")
    val language: NamedResource? = null,

    @SerializedName("version")
    val version: NamedResource? = null
) : Parcelable