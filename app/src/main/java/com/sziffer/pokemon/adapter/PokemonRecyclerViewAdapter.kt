package com.sziffer.pokemon.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sziffer.pokemon.PokemonDetailsActivity
import com.sziffer.pokemon.R
import com.sziffer.pokemon.model.PokemonManager
import com.sziffer.pokemon.model.pokemon.Pokemon

class PokemonRecyclerViewAdapter(
    private var pokemonList: ArrayList<Pokemon> = ArrayList()
) : RecyclerView.Adapter<PokemonRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(
        itemView: View,
        private val recyclerViewOnClickListener: RecyclerViewOnClickListener
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val pokemonHolder: LinearLayout =
            itemView.findViewById(R.id.pokemonHolderLinearLayout)

        val nameTextView: TextView = itemView.findViewById(R.id.detailsLinearLayout)

        override fun onClick(p0: View?) {
            recyclerViewOnClickListener.itemClicked(itemView, this.layoutPosition)
        }

        init {
            pokemonHolder.setOnClickListener(this)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val context = parent.context
        val itemView: View = LayoutInflater.from(context)
            .inflate(R.layout.pokemon_item,parent,false)

        return ViewHolder(itemView, object : RecyclerViewOnClickListener {
            override fun itemClicked(v: View, pos: Int) {
                PokemonManager.setPokemon(pokemonList[pos])
                context.startActivity(
                    Intent(context, PokemonDetailsActivity::class.java)
                )
            }
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            nameTextView.text = pokemonList[position].name
        }
    }

    override fun getItemCount(): Int {
        return pokemonList.count()
    }

    fun addPokemon(pokemon: ArrayList<Pokemon>) {
        this.pokemonList.addAll(pokemon)
        this.notifyDataSetChanged()
    }
}
interface RecyclerViewOnClickListener {
    fun itemClicked(v: View, pos: Int)
}