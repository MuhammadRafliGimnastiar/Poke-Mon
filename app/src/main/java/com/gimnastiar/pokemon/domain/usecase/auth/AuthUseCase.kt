package com.gimnastiar.pokemon.domain.usecase.auth

import com.gimnastiar.pokemon.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthUseCase {
    suspend fun registUser(user: User, password: String): Long

    fun loginUser(email: String, password: String): Flow<User?>
}