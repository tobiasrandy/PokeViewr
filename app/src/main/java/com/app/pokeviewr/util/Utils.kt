package com.app.pokeviewr.util

import com.app.pokeviewr.R
import java.util.*
import kotlin.math.sqrt

fun String.capitalizeFirstChar(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }
}

fun getTypeDrawable(typeName: String): Int {
    return when (typeName.lowercase()) {
        "fire" -> R.drawable.ic_fire
        "water" -> R.drawable.ic_water
        "grass" -> R.drawable.ic_grass
        "electric" -> R.drawable.ic_electric
        "bug" -> R.drawable.ic_bug
        "rock" -> R.drawable.ic_rock
        "ghost" -> R.drawable.ic_ghost
        "dragon" -> R.drawable.ic_dragon
        "psychic" -> R.drawable.ic_psychic
        "ice" -> R.drawable.ic_ice
        "dark" -> R.drawable.ic_dark
        "fairy" -> R.drawable.ic_fairy
        "normal" -> R.drawable.ic_normal
        "fighting" -> R.drawable.ic_fighting
        "flying" -> R.drawable.ic_flying
        "poison" -> R.drawable.ic_poison
        "ground" -> R.drawable.ic_ground
        "steel" -> R.drawable.ic_steel
        else -> R.drawable.ic_pokeball
    }
}

fun isShinyPokemon(): Boolean {
    val randomNumber = (0..99).random()
    return randomNumber < 10
}

fun isPrime(number: Int): Boolean {
    if (number <= 1) return false
    if (number == 2) return true
    if (number % 2 == 0) return false

    for (i in 3..sqrt(number.toDouble()).toInt() step 2) {
        if (number % i == 0) return false
    }
    return true
}