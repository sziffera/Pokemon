package com.sziffer.pokemon.model

import com.sziffer.pokemon.model.pokemon.PokemonDetails

interface PokemonDetailsCallback {
    fun pokemonDetailsFetched(pokemon: PokemonDetails?)
}