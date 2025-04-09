package com.gimnastiar.pokemon.domain.repository

import com.gimnastiar.pokemon.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {
    suspend fun registUser(user: User, password: String): Long

    fun loginUser(email: String, password: String): Flow<User?>
}