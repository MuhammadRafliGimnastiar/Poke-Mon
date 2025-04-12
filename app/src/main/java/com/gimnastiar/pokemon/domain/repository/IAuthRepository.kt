package com.gimnastiar.pokemon.domain.repository

import com.gimnastiar.pokemon.data.LoginResult
import com.gimnastiar.pokemon.domain.model.User

interface IAuthRepository {
    suspend fun registUser(user: User, password: String): Long

    suspend fun findUserByEmail(email: String, password: String): LoginResult
}