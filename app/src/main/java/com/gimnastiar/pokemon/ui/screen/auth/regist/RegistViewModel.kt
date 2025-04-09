package com.gimnastiar.pokemon.ui.screen.auth.regist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.gimnastiar.pokemon.domain.model.User
import com.gimnastiar.pokemon.domain.usecase.auth.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistViewModel @Inject constructor(
    private val useCase: AuthUseCase
) : ViewModel() {

    private val _registResponse = MutableLiveData<Long>(0)
    val registResponse : LiveData<Long> get() = _registResponse

    fun regist(user: User, password: String)
    = viewModelScope.launch {
        _registResponse.value = useCase.registUser(user, password)
    }
}