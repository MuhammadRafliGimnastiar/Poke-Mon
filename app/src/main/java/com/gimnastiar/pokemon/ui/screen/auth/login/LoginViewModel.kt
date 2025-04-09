package com.gimnastiar.pokemon.ui.screen.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gimnastiar.pokemon.data.preferences.SessionDatastore
import com.gimnastiar.pokemon.domain.model.User
import com.gimnastiar.pokemon.domain.usecase.auth.AuthUseCase
import com.gimnastiar.pokemon.utils.Helper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val sessionDataStore: SessionDatastore
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(
        value = null
    )
    val user: StateFlow<User?> get() = _user

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authUseCase.loginUser(email, password).collectLatest {
                _user.value = it
            }
        }
    }

    fun saveSession(user: User) = viewModelScope.launch {
        val dummyToken = Helper.generateDummyToken(user.email)
        sessionDataStore.saveSession(user, dummyToken)
    }
}