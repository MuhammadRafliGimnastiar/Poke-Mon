package com.gimnastiar.pokemon.data.source.local.auth

import com.gimnastiar.pokemon.data.source.local.auth.entity.UserEntitry
import com.gimnastiar.pokemon.data.source.local.auth.room.UserDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataSource @Inject constructor(
    private val dao: UserDao
) {
    suspend fun registUser(user: UserEntitry): Flow<Long> = dao.registUser(user)

    suspend fun loginUser(email: String, password: String): Flow<UserEntitry?> = dao.loginUser(email, password)
}