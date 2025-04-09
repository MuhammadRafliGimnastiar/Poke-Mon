package com.gimnastiar.pokemon.domain.usecase.auth

import com.gimnastiar.pokemon.data.source.local.auth.entity.UserEntity
import com.gimnastiar.pokemon.domain.model.User
import com.gimnastiar.pokemon.domain.repository.IAuthRepository
import com.gimnastiar.pokemon.utils.Helper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val repository: IAuthRepository
): AuthUseCase{
    override suspend fun registUser(user: User, password: String): Long
    = repository.registUser(user, Helper.hashPassword(password))

    override fun loginUser(email: String, password: String): Flow<User?>
    = repository.loginUser(email, Helper.hashPassword(password))
}