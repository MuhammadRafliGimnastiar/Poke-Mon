package com.gimnastiar.pokemon.data

import android.util.Log
import androidx.paging.PagingData
import androidx.paging.map
import com.gimnastiar.pokemon.data.source.local.pokemon.entity.PokemonEntity
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

    override fun getPokemonListSearch(query: String): Flow<PagingData<PokemonList>> {
        return remoteDataSource.getPokemonListSearch(query)
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

            override suspend fun loadFromNetwork(data: PokemonDetail): Flow<Pokemon> {
                Log.i("REPO LOCAL save data load", data.name.toString())
                localDataSource.insertWithLimit(DataMapper.mapResponseToEntityPokemon(data))
                return DataMapper.mapResponseToDomainPokemon(data)
            }

        }.asFlow()

    override fun getAllPokemonFlow(): Flow<List<Pokemon>> {
        val data = DataMapper.mapEntityToDomainPokemon(localDataSource.getAllPokemonFlow())
        Log.i("REPO LOCAL DATA", data.toString())
        return data
    }


}

