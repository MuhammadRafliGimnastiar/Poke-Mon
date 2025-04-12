package com.gimnastiar.pokemon.ui.screen.auth.regist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
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
class RegistViewModel @Inject constructor(
    private val useCase: AuthUseCase,
    private val sessionDataStore: SessionDatastore
) : ViewModel() {

    private val _registResponse = MutableLiveData<Long>()
    val registResponse : LiveData<Long> get() = _registResponse

    fun regist(user: User, password: String)
    = viewModelScope.launch {
        Log.i("RETURN REGIST DATA vm", user.email)
        _registResponse.value = useCase.registUser(user, password)
    }

    fun saveSession(user: User) = viewModelScope.launch {
        val dummyToken = Helper.generateDummyToken(user.email)
        sessionDataStore.saveSession(user, dummyToken)
    }
}