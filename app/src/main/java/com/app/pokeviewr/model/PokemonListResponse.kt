package com.app.pokeviewr.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonListResponse(
    @field:SerializedName("count")
    val count: Int? = null,

    @field:SerializedName("next")
    val next: String? = null,

    @field:SerializedName("previous")
    val previous: String? = null,

    @field:SerializedName("results")
    val results: MutableList<NamedResource>? = null
) : Parcelable
