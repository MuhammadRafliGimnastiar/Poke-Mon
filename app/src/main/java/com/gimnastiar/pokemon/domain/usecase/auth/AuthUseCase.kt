package com.gimnastiar.pokemon.domain.usecase.auth

import com.gimnastiar.pokemon.data.source.local.auth.entity.UserEntitry
import kotlinx.coroutines.flow.Flow

interface AuthUseCase {
    suspend fun registUser(user: UserEntitry): Flow<Long>

    suspend fun loginUser(email: String, password: String): Flow<UserEntitry?>
}