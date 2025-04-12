package com.gimnastiar.pokemon.data.source.remote.response

import com.gimnastiar.pokemon.domain.model.PokemonList
import com.google.gson.annotations.SerializedName

data class PokemonListResponse(

	@field:SerializedName("next")
	val next: String? = null,

	@field:SerializedName("previous")
	val previous: Any? = null,

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("results")
	val results: List<PokemonResult?>? = null
)

data class PokemonResult(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)

fun PokemonResult.toPokemonList(): PokemonList {
	return PokemonList(
		name = name,
		url = url
	)
}
