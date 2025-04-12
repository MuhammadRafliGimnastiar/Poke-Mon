package com.gimnastiar.pokemon.data.source.local.pokemon.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon",
    indices = [Index(value = ["name"], unique = true)])
data class PokemonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val name: String,
    val imageUrl: String,
    val abilities: List<String?> = listOf(null),
    val weight: Int,
    val height: Int
)
