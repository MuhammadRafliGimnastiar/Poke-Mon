package com.gimnastiar.pokemon.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gimnastiar.pokemon.databinding.ItemPokemonBinding
import com.gimnastiar.pokemon.domain.model.PokemonList

class PokemonListAdapter :
    PagingDataAdapter<PokemonList, PokemonListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)

        if (data != null) {
            holder.bind(data)
            holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(data) }
        }
    }

    class MyViewHolder(private val binding: ItemPokemonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: PokemonList) {
            binding.tvTitlePokemon.text = data.name
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PokemonList>() {
            override fun areItemsTheSame(oldItem: PokemonList, newItem: PokemonList): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: PokemonList, newItem: PokemonList): Boolean {
                return oldItem.url == newItem.url
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: PokemonList)
    }
}