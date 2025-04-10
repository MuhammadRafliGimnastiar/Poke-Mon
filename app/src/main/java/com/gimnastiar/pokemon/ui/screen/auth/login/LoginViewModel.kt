package com.gimnastiar.pokemon.ui.screen.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gimnastiar.pokemon.data.LoginResult
import com.gimnastiar.pokemon.data.preferences.SessionDatastore
import com.gimnastiar.pokemon.domain.model.User
import com.gimnastiar.pokemon.domain.usecase.auth.AuthUseCase
import com.gimnastiar.pokemon.utils.Helper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val sessionDataStore: SessionDatastore
) : ViewModel() {

    private val _loginResult = MutableSharedFlow<LoginResult>(replay = 0)
    val loginResult: SharedFlow<LoginResult> get() = _loginResult

    fun login(email: String, password: String) = viewModelScope.launch {
        _loginResult.emit(authUseCase.findUserByEmail(email, password))
    }

    private val _messageEvent = MutableSharedFlow<String>(replay = 0)
    val messageEvent: SharedFlow<String> get() = _messageEvent

    fun triggerMessage(message: String) {
        viewModelScope.launch {
            _messageEvent.emit(message)
        }
    }

    fun saveSession(user: User) = viewModelScope.launch {
        val dummyToken = Helper.generateDummyToken(user.email)
        sessionDataStore.saveSession(user, dummyToken)
    }
}