package com.gimnastiar.pokemon.data.source.local.pokemon.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val name: String,
    val imageUrl: String,
    val abilities: List<String?> = listOf(null),
    val weight: Int,
    val height: Int
)
