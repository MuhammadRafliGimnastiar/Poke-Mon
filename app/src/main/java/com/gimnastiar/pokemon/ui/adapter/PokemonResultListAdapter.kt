package com.gimnastiar.pokemon.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gimnastiar.pokemon.data.source.remote.response.PokemonResult
import com.gimnastiar.pokemon.databinding.ItemPokemonBinding

class PokemonResultListAdapter :
    PagingDataAdapter<PokemonResult, PokemonResultListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        Log.d("Adapter", "Bind item: ${data?.name}")

        if (data != null) {
            holder.bind(data)
        }
    }

    class MyViewHolder(private val binding: ItemPokemonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: PokemonResult) {
            binding.tvTitlePokemon.text = data.name
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PokemonResult>() {
            override fun areItemsTheSame(oldItem: PokemonResult, newItem: PokemonResult): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: PokemonResult, newItem: PokemonResult): Boolean {
                return oldItem.url == newItem.url
            }
        }
    }
}