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

    //the url of the next page, null if no more available
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
    //sets the listener for the api call
    fun setCallBack(pokemonDetailsCallback: PokemonDetailsCallback) {
        this.pokemonDetailsCallback = pokemonDetailsCallback
    }
    /**
     * When the user pressed the back button before the details have been fetched
     * cancels the call to avoid false information and unnecessary fetching.
     * Also sets the listener to null
     * */
    fun cancelDetailsCall() {
        pokemonDetailsCallback = null
        if (pokemonDetailsCall != null) {
            if (!pokemonDetailsCall!!.isExecuted) {
                pokemonDetailsCall!!.cancel()
                Log.d("MANAGER", pokemonDetailsCall!!.isCanceled.toString())
            }
        }
    }

    /**
     * Fetches the pokemon details based on the clicked pokemon's url.
     * When the call is done, notifies the listener
     */
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

    /**
     * Called when the user opens the app and fetches the first page of the pokemon list
     * When the data is fetched, sets the RecyclerView's list
     */
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
    //Called when a new page is needed, returns if the nextUrl is null (no more page available)
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