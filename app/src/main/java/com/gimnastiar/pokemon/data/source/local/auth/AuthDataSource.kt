package com.gimnastiar.pokemon.data.source.local.auth

import com.gimnastiar.pokemon.data.source.local.auth.entity.UserEntity
import com.gimnastiar.pokemon.data.source.local.auth.room.UserDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataSource @Inject constructor(
    private val dao: UserDao
) {
    suspend fun registUser(user: UserEntity): Long = dao.registUser(user)

    suspend fun loginUser(email: String, password: String): UserEntity?= dao.loginUser(email, password)

    suspend fun findUserByEmail(email: String): UserEntity?= dao.findUserByEmail(email)
}