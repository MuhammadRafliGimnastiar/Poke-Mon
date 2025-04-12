package com.gimnastiar.pokemon.ui.screen.auth.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gimnastiar.pokemon.data.preferences.SessionDatastore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel@Inject constructor(
    private val sessionDataStore: SessionDatastore
): ViewModel() {
    fun getUser() = sessionDataStore.getSession()

}