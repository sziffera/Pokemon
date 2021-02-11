package com.sziffer.pokemon.remote


import com.sziffer.pokemon.model.pokemon.ApiResponse
import com.sziffer.pokemon.model.pokemon.PokemonDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface PokeApiService {

    @GET("pokemon")
    fun getPokemonList(): Call<ApiResponse>

    @GET("pokemon/{name}")
    fun getPokemonByName(
        @Path("name") name: String
    ): Call<PokemonDetails>

    @GET
    fun getPokemonListByUrl(
            @Url url: String
    ): Call<ApiResponse>
}