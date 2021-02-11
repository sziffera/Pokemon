package com.sziffer.pokemon.model.pokemon

import com.google.gson.annotations.SerializedName

data class ApiResponse(
        @SerializedName("results") val pokemonList: ArrayList<Pokemon>,
        val previous: String?,
        val next: String?,
)
