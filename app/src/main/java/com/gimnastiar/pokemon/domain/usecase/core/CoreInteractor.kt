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

    override fun getDetailPokemon(url: String): Flow<Resource<Pokemon>> =
        repo.getDetailPokemon(url)
}