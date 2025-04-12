package com.gimnastiar.pokemon.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gimnastiar.pokemon.databinding.ItemAbilitiesBinding

class AbilitiesListAdapter(
    private val items: List<String>
) : RecyclerView.Adapter<AbilitiesListAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemAbilitiesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            with(binding) {
                tvAbilities.text = item
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAbilitiesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}