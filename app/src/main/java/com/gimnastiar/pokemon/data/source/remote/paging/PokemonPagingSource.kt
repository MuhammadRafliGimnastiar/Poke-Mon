package com.gimnastiar.pokemon.data.source.remote.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gimnastiar.pokemon.data.source.remote.network.ApiService
import com.gimnastiar.pokemon.data.source.remote.response.PokemonResult

class PokemonPagingSource(
    private val apiService: ApiService
) : PagingSource<Int, PokemonResult>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonResult> {
        return try {
            val offset = params.key ?: 0
            val limit = params.loadSize

            val response = apiService.getPokemon(limit = limit, offset = offset)

            val prevOffset = if (offset == 0) null else offset - limit
            val nextOffset = if (response.next != null) offset + limit else null

            Log.d("PagingSource", "offset: $offset, limit: $limit, resultCount: ${response.results?.size}")


            LoadResult.Page(
                data = response.results.orEmpty().filterNotNull(),
                prevKey = prevOffset,
                nextKey = nextOffset
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PokemonResult>): Int? {
        return state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            val pageSize = state.config.pageSize
            anchorPage?.prevKey?.plus(pageSize)
                ?: anchorPage?.nextKey?.minus(pageSize)
        }
    }
}