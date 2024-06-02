package com.app.pokeviewr.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class NamedResource(
    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("url")
    val url: String? = null
) : Parcelable