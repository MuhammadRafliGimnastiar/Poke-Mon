package com.gimnastiar.pokemon.data.source.local.auth

import com.gimnastiar.pokemon.data.source.local.auth.entity.UserEntity
import com.gimnastiar.pokemon.data.source.local.auth.room.UserDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataSource @Inject constructor(
    private val dao: UserDao
) {
    suspend fun registUser(user: UserEntity): Long = dao.registUser(user)

    suspend fun findUserByEmail(email: String): UserEntity?= dao.findUserByEmail(email)
}