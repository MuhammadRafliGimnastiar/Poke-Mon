package com.gimnastiar.pokemon.data

import com.gimnastiar.pokemon.data.source.local.auth.AuthDataSource
import com.gimnastiar.pokemon.domain.model.User
import com.gimnastiar.pokemon.domain.repository.IAuthRepository
import com.gimnastiar.pokemon.utils.DataMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: AuthDataSource
) : IAuthRepository {
    override suspend fun registUser(user: User, password: String): Long {
        val response = auth.registUser(DataMapper.mapDomainToEntityUser(user, password))
        return response
    }


    override suspend fun findUserByEmail(email: String, password: String): LoginResult {
        val user = auth.findUserByEmail(email)

        return when {
            user == null -> LoginResult.UserNotFound
            user.password != password -> LoginResult.WrongPassword
            else -> LoginResult.Success(DataMapper.mapEntityToDomainUser(user))
        }
    }

}