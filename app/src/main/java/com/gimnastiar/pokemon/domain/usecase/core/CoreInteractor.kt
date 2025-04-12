package com.gimnastiar.pokemon.domain.usecase.core

import androidx.paging.PagingData
import com.gimnastiar.pokemon.data.Resource
import com.gimnastiar.pokemon.domain.model.Pokemon
import com.gimnastiar.pokemon.domain.model.PokemonList
import com.gimnastiar.pokemon.domain.repository.ICoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CoreInteractor @Inject constructor(
    private val repo: ICoreRepository
): CoreUseCase {
    override fun getPokemonList(): Flow<PagingData<PokemonList>> =
        repo.getPokemonList()

    override fun getPokemonListSearch(query: String): Flow<PagingData<PokemonList>> =
        repo.getPokemonListSearch(query)


    override fun getDetailPokemon(url: String): Flow<Resource<Pokemon>> =
        repo.getDetailPokemon(url)

    override fun getAllPokemonFlow(): Flow<List<Pokemon>> =
        repo.getAllPokemonFlow()
}