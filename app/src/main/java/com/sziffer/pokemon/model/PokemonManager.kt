package com.sziffer.pokemon.model

import android.util.Log
import com.sziffer.pokemon.utils.BASE_URL
import com.sziffer.pokemon.adapter.PokemonRecyclerViewAdapter
import com.sziffer.pokemon.model.pokemon.ApiResponse
import com.sziffer.pokemon.model.pokemon.Pokemon
import com.sziffer.pokemon.model.pokemon.PokemonDetails
import com.sziffer.pokemon.remote.PokeApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PokemonManager {

    var nextUrl: String? = null
        private set

    private var pokemon: Pokemon? = null
    private var pokemonDetailsCallback: PokemonDetailsCallback? = null

    private var pokemonDetailsCall: Call<PokemonDetails>? = null

    private val retrofit by lazy {
        Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private val pokemonApiInterface by lazy {
        retrofit.create(PokeApiService::class.java)
    }

    fun setPokemon(pokemon: Pokemon) {
        this.pokemon = pokemon
    }

    fun setCallBack(pokemonDetailsCallback: PokemonDetailsCallback) {
        this.pokemonDetailsCallback = pokemonDetailsCallback
    }

    fun cancelDetailsCall() {
        pokemonDetailsCallback = null
        if (pokemonDetailsCall != null) {
            if (!pokemonDetailsCall!!.isExecuted) {
                pokemonDetailsCall!!.cancel()
                Log.d("MANAGER", pokemonDetailsCall!!.isCanceled.toString())
            }
        }
    }

    fun getPokemonDetails() {

        if (pokemon == null)
            return

        pokemonDetailsCall = pokemonApiInterface.getPokemonByName(pokemon!!.name)

        pokemonDetailsCall!!.enqueue(object : Callback<PokemonDetails> {
            override fun onResponse(
                call: Call<PokemonDetails>,
                response: Response<PokemonDetails>
            ) {
                pokemonDetailsCallback?.pokemonDetailsFetched(response.body())
            }

            override fun onFailure(call: Call<PokemonDetails>, t: Throwable) {
                pokemonDetailsCallback?.pokemonDetailsFetched(null)
            }
        })
    }

    fun getPokemonList(pokemonRecyclerViewAdapter: PokemonRecyclerViewAdapter) {

        val call = pokemonApiInterface.getPokemonList()

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {

                nextUrl = response.body()?.next

                Log.d("MANAGER",response.body().toString())
                response.body()?.pokemonList?.let { pokemonRecyclerViewAdapter.addPokemon(it) }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d("MANAGER",t.localizedMessage.toString())
            }

        })
    }

    fun getNextPage(pokemonRecyclerViewAdapter: PokemonRecyclerViewAdapter) {

        if (nextUrl == null)
            return
        val call = pokemonApiInterface.getPokemonListByUrl(nextUrl!!)
        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                nextUrl = response.body()?.next
                response.body()?.pokemonList?.let { pokemonRecyclerViewAdapter.addPokemon(it)}
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d("MANAGER",t.localizedMessage.toString())
            }
        })
    }
}