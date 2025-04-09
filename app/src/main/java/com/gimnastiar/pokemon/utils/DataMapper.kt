package com.gimnastiar.pokemon.utils

import com.gimnastiar.pokemon.data.source.local.auth.entity.UserEntity
import com.gimnastiar.pokemon.domain.model.User
import kotlinx.coroutines.flow.Flow
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
}