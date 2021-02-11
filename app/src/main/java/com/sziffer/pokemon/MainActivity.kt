package com.sziffer.pokemon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sziffer.pokemon.adapter.EndlessRecyclerViewScrollListener
import com.sziffer.pokemon.adapter.PokemonRecyclerViewAdapter
import com.sziffer.pokemon.databinding.ActivityMainBinding
import com.sziffer.pokemon.model.PokemonManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var pokemonAdapter: PokemonRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pokemonAdapter = PokemonRecyclerViewAdapter()

        val gridLayoutManager = GridLayoutManager(this, 2)

        scrollListener = object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, recyclerView: RecyclerView) {
                PokemonManager.getNextPage(pokemonAdapter)
            }
        }

        with(binding.pokemonRecyclerView) {
            adapter = pokemonAdapter
            layoutManager = gridLayoutManager
            addOnScrollListener(scrollListener)
        }

        PokemonManager.getPokemonList(pokemonAdapter)

    }
}