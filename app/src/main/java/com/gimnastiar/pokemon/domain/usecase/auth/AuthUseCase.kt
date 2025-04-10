package com.gimnastiar.pokemon.domain.usecase.auth

import com.gimnastiar.pokemon.data.LoginResult
import com.gimnastiar.pokemon.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthUseCase {
    suspend fun registUser(user: User, password: String): Long

    suspend fun findUserByEmail(email: String, password: String): LoginResult
}