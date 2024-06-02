package com.app.pokeviewr.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class Pokemon (
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "species") val species: String,
    @ColumnInfo(name = "is_shiny") val isShiny: Boolean,
    @ColumnInfo(name = "ability") val ability: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "type1") val type1: String,
    @ColumnInfo(name = "type2") val type2: String
)