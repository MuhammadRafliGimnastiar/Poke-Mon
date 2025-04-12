package com.gimnastiar.pokemon.ui.screen.auth.splash

import androidx.lifecycle.ViewModel
import com.gimnastiar.pokemon.data.preferences.SessionDatastore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel@Inject constructor(
    private val sessionDataStore: SessionDatastore
): ViewModel() {
    fun getUser() = sessionDataStore.getSession()

}