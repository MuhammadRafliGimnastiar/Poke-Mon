package com.gimnastiar.pokemon.data.source.remote

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gimnastiar.pokemon.data.source.remote.network.ApiResponse
import com.gimnastiar.pokemon.data.source.remote.network.ApiService
import com.gimnastiar.pokemon.data.source.remote.paging.PokemonPagingSource
import com.gimnastiar.pokemon.data.source.remote.response.PokemonDetail
import com.gimnastiar.pokemon.data.source.remote.response.PokemonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    fun getPokemonList(): Flow<PagingData<PokemonResult>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PokemonPagingSource(apiService) }
        ).flow
    }

    fun getDetailPokemon(url: String): Flow<ApiResponse<PokemonDetail>> {
//        return apiService.getPokemonDetail(url)
        return flow {
            try {
                val response = apiService.getPokemonDetail(url)
                val result = response.name
                if (result!!.isNotEmpty()) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}