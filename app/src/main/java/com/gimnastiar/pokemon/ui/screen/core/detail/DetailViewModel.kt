package com.gimnastiar.pokemon.ui.screen.core.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.gimnastiar.pokemon.data.Resource
import com.gimnastiar.pokemon.domain.model.Pokemon
import com.gimnastiar.pokemon.domain.usecase.core.CoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.internal.userAgent
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val useCase: CoreUseCase
): ViewModel() {

    private val _pokemon = MutableLiveData<Resource<Pokemon>>()
    val pokemon: LiveData<Resource<Pokemon>> get() = _pokemon

    fun getPokemon(url: String) = useCase.getDetailPokemon(url).asLiveData()
}