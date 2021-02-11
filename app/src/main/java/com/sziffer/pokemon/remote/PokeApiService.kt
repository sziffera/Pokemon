package com.sziffer.pokemon.remote


import com.sziffer.pokemon.model.pokemon.ApiResponse
import com.sziffer.pokemon.model.pokemon.PokemonDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface PokeApiService {
    
    //fetches the first page
    @GET("pokemon")
    fun getPokemonList(): Call<ApiResponse>

    //fetches the pokemon by name
    @GET("pokemon/{name}")
    fun getPokemonByName(
        @Path("name") name: String
    ): Call<PokemonDetails>

    //fetches the next page based on the next url
    @GET
    fun getPokemonListByUrl(
            @Url url: String
    ): Call<ApiResponse>
}