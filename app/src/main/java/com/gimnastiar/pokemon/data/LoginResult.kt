package com.gimnastiar.pokemon.data

import com.gimnastiar.pokemon.data.source.local.auth.entity.UserEntity
import com.gimnastiar.pokemon.domain.model.User

sealed class LoginResult {
    data class Success(val user: User) : LoginResult()
    object UserNotFound : LoginResult()
    object WrongPassword : LoginResult()
}