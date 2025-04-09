package com.gimnastiar.pokemon.data

import com.gimnastiar.pokemon.data.preferences.SessionDatastore
import com.gimnastiar.pokemon.data.source.local.auth.AuthDataSource
import com.gimnastiar.pokemon.domain.model.User
import com.gimnastiar.pokemon.domain.repository.IAuthRepository
import com.gimnastiar.pokemon.utils.DataMapper
import com.gimnastiar.pokemon.utils.Helper.generateDummyToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: AuthDataSource,
    private val sessionDatastore: SessionDatastore
) : IAuthRepository {
    override suspend fun registUser(user: User, password: String): Long =
        auth.registUser(DataMapper.mapDomainToEntityUser(user, password))

    override fun loginUser(email: String, password: String): Flow<User?> {
        return auth.loginUser(email, password)
            .map { entity ->
                entity?.let {
                    DataMapper.mapEntityToDomainUser(it)
                }
            }
    }

}