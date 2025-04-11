package com.gimnastiar.pokemon.data

import androidx.paging.PagingData
import androidx.paging.map
import com.gimnastiar.pokemon.data.source.local.pokemon.room.PokemonDao
import com.gimnastiar.pokemon.data.source.remote.RemoteDataSource
import com.gimnastiar.pokemon.data.source.remote.network.ApiResponse
import com.gimnastiar.pokemon.data.source.remote.response.PokemonDetail
import com.gimnastiar.pokemon.data.source.remote.response.PokemonResult
import com.gimnastiar.pokemon.data.source.remote.response.toPokemonList
import com.gimnastiar.pokemon.domain.model.Pokemon
import com.gimnastiar.pokemon.domain.model.PokemonList
import com.gimnastiar.pokemon.domain.repository.ICoreRepository
import com.gimnastiar.pokemon.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoreRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: PokemonDao
): ICoreRepository {
    override fun getPokemonList(): Flow<PagingData<PokemonList>> {
        return remoteDataSource.getPokemonList()
            .map { pagingData ->
                pagingData.map {
                    it.toPokemonList()

                }
            }
    }

    override fun getDetailPokemon(url: String): Flow<Resource<Pokemon>> =
        object: NetworkBoundResource<Pokemon, PokemonDetail>() {
            override suspend fun createCall(): Flow<ApiResponse<PokemonDetail>> =
                remoteDataSource.getDetailPokemon(url)

            override fun loadFromNetwork(data: PokemonDetail): Flow<Pokemon> =
                DataMapper.mapResponseToDomainPokemon(data)

            override suspend fun saveCallResult(data: PokemonDetail) =
                localDataSource.insertWithLimit(DataMapper.mapResponseToEntityPokemon(data))
        }.asFlow()

}