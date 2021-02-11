package com.sziffer.pokemon.model.pokemon

import com.google.gson.annotations.SerializedName

data class Sprite(
    @SerializedName("front_default") val imagePath: String
)
