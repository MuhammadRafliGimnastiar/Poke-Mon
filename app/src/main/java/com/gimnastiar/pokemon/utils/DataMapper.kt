package com.gimnastiar.pokemon.utils

import com.gimnastiar.pokemon.data.source.local.auth.entity.UserEntity
import com.gimnastiar.pokemon.data.source.local.pokemon.entity.PokemonEntity
import com.gimnastiar.pokemon.data.source.remote.response.PokemonDetail
import com.gimnastiar.pokemon.data.source.remote.response.PokemonResult
import com.gimnastiar.pokemon.domain.model.Pokemon
import com.gimnastiar.pokemon.domain.model.PokemonList
import com.gimnastiar.pokemon.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

object DataMapper {
    fun mapEntityToDomainUser(input: UserEntity) : User {
        return User(
            id = input.id,
            name = input.name,
            email = input.email
        )
    }

    fun mapDomainToEntityUser(user: User, password: String): UserEntity {
        return UserEntity(
            email = user.email,
            name = user.name,
            password = password
        )
    }

    fun mapEntityToDomainPokemonList(data: PokemonResult): PokemonList {
        return PokemonList(
            name = data.name,
            url = data.url
        )
    }

    fun mapResponseToDomainPokemon(data: PokemonDetail): Flow<Pokemon> {
        val pokemon =  Pokemon(
            name = data.name.orEmpty(),
            abilities = data.abilities?.mapNotNull { it?.ability?.name } ?: emptyList(),
            imageUrl = data.sprites?.other?.officialArtwork?.frontDefault.orEmpty(),
            height = data.height ?: 0,
            weight = data.weight ?: 0,
        )

        return flowOf(pokemon)
    }

    fun mapResponseToEntityPokemon(data: PokemonDetail): PokemonEntity {
        return PokemonEntity(
            name = data.name.orEmpty(),
            abilities = data.abilities?.mapNotNull { it?.ability?.name } ?: emptyList(),
            imageUrl = data.sprites?.other?.officialArtwork?.frontDefault.orEmpty(),
            height = data.height ?: 0,
            weight = data.weight ?: 0,
        )
    }

    fun mapEntityToDomainPokemon(data: Flow<List<PokemonEntity>>): Flow<List<Pokemon>> {
        return data.map { entityList ->
            entityList.map { it.toDomain() }
        }
    }

    fun PokemonEntity.toDomain(): Pokemon {
        return Pokemon(
            name = name,
            imageUrl = imageUrl,
            abilities = abilities,
            weight = weight,
            height = height
        )
    }

}