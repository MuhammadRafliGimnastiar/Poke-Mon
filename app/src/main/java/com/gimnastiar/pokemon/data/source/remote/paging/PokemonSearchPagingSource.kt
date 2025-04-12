package com.gimnastiar.pokemon.data.source.remote.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gimnastiar.pokemon.data.source.remote.network.ApiService
import com.gimnastiar.pokemon.data.source.remote.response.PokemonResult

class PokemonSearchPagingSource(
    private val apiService: ApiService,
    private val query: String
) : PagingSource<Int, PokemonResult>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonResult> = try {
        val offset = params.key ?: DEFAULT_OFFSET
        val limit = params.loadSize

        val response = apiService.getPokemon(limit = limit, offset = offset)

        val results = response.results.orEmpty()
            .filterNotNull()
            .filter { it.name!!.contains(query, ignoreCase = true) }


        val prevOffset = getPrevKey(offset, limit)
        val nextOffset = getNextKey(offset, limit, response.next)

        Log.d("PagingSourceSearch", "offset: $offset, limit: $limit, query: '$query', resultCount: ${results.size}")

        LoadResult.Page(
            data = results,
            prevKey = prevOffset,
            nextKey = nextOffset
        )

    } catch (e: Exception) {
        LoadResult.Error(e)
    }

    override fun getRefreshKey(state: PagingState<Int, PokemonResult>): Int? {
        return state.anchorPosition?.let { anchor ->
            val page = state.closestPageToPosition(anchor)
            page?.prevKey?.plus(state.config.pageSize)
                ?: page?.nextKey?.minus(state.config.pageSize)
        }
    }

    private fun getPrevKey(offset: Int, limit: Int): Int? =
        if (offset == DEFAULT_OFFSET) null else offset - limit

    private fun getNextKey(offset: Int, limit: Int, nextUrl: String?): Int? =
        nextUrl?.let { offset + limit }

    companion object {
        private const val DEFAULT_OFFSET = 0
    }
}