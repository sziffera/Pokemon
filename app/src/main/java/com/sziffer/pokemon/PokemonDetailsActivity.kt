package com.sziffer.pokemon

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.sziffer.pokemon.databinding.ActivityPokemonDetailsBinding
import com.sziffer.pokemon.model.PokemonDetailsCallback
import com.sziffer.pokemon.model.PokemonManager
import com.sziffer.pokemon.model.pokemon.PokemonDetails
import java.util.*

class PokemonDetailsActivity : AppCompatActivity(), PokemonDetailsCallback {

    private lateinit var binding: ActivityPokemonDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPokemonDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        PokemonManager.setCallBack(this)
        PokemonManager.getPokemonDetails()
    }

    @SuppressLint("SetTextI18n")
    override fun pokemonDetailsFetched(pokemon: PokemonDetails?) {
        if (pokemon == null) {
            Toast.makeText(this,getString(R.string.cannot_fetch),Toast.LENGTH_SHORT).show()
        }
        with(binding) {
            nameTextView.text = pokemon!!.name.toUpperCase(Locale.ROOT)
            weightTextView.text = "${getString(R.string.weight)}: ${pokemon.weight}"
            heightTextView.text = "${getString(R.string.height)}: ${pokemon.weight}"
            Picasso.get().load(pokemon.sprites.imagePath).into(imageView)
        }
        var abilities: String = getString(R.string.non_hidden) + ":\n"
        for (ability in pokemon!!.abilities) {
            if (ability.isHidden)
                abilities += ability.ability.name + "\n"
        }
        binding.abilitiesTextView.text = abilities
    }

    override fun onBackPressed() {
        PokemonManager.cancelDetailsCall()
        super.onBackPressed()
    }
}