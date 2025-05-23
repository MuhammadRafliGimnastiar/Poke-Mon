package com.gimnastiar.pokemon.domain.usecase.core

import androidx.paging.PagingData
import com.gimnastiar.pokemon.data.Resource
import com.gimnastiar.pokemon.domain.model.Pokemon
import com.gimnastiar.pokemon.domain.model.PokemonList
import kotlinx.coroutines.flow.Flow

interface CoreUseCase {
    fun getPokemonList(): Flow<PagingData<PokemonList>>

    fun getPokemonListSearch(query: String): Flow<PagingData<PokemonList>>

    fun getDetailPokemon(url: String): Flow<Resource<Pokemon>>

    fun getAllPokemonFlow(): Flow<List<Pokemon>>
}