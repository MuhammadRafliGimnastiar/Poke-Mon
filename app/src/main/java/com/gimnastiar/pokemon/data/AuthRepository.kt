package com.gimnastiar.pokemon.data

import com.gimnastiar.pokemon.data.source.local.auth.AuthDataSource
import com.gimnastiar.pokemon.data.source.local.auth.entity.UserEntitry
import com.gimnastiar.pokemon.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: AuthDataSource
): IAuthRepository {
    override suspend fun registUser(user: UserEntitry): Flow<Long>
    = auth.registUser(user)

    override suspend fun loginUser(email: String, password: String): Flow<UserEntitry?>
    = auth.loginUser(email, password)

}