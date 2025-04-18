package com.gimnastiar.pokemon.domain.usecase.auth

import com.gimnastiar.pokemon.data.LoginResult
import com.gimnastiar.pokemon.domain.model.User
import com.gimnastiar.pokemon.domain.repository.IAuthRepository
import com.gimnastiar.pokemon.utils.Helper
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val repository: IAuthRepository
): AuthUseCase{
    override suspend fun registUser(user: User, password: String): Long
    = repository.registUser(user, Helper.hashPassword(password))

    override suspend fun findUserByEmail(email: String, password: String): LoginResult
    = repository.findUserByEmail(email, Helper.hashPassword(password))
}