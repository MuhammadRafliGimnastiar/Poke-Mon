package com.gimnastiar.pokemon.domain.usecase.auth

import com.gimnastiar.pokemon.data.source.local.auth.entity.UserEntitry
import com.gimnastiar.pokemon.domain.repository.IAuthRepository
import com.gimnastiar.pokemon.utils.Helper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val repository: IAuthRepository
): AuthUseCase{
    override suspend fun registUser(user: UserEntitry): Flow<Long>
    = repository.registUser(
        UserEntitry(
            email = user.email,
            name = user.name,
            password = Helper.hashPassword(user.password)
        )
    )

    override suspend fun loginUser(email: String, password: String): Flow<UserEntitry?>
    = repository.loginUser(email, Helper.hashPassword(password))
}