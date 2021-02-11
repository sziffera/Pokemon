package com.sziffer.pokemon.model.pokemon

data class PokemonDetails(
    val name: String,
    val height: Int,
    val weight: Int,
    val abilities: ArrayList<AbilityBase>,
    val sprites: Sprite
)

