package com.sziffer.pokemon.model.pokemon

import com.google.gson.annotations.SerializedName

data class AbilityBase(
    @SerializedName("is_hidden") val isHidden: Boolean,
    val ability: Ability
)
