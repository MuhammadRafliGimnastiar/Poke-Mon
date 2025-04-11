package com.gimnastiar.pokemon.ui.screen.core.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gimnastiar.pokemon.data.source.remote.response.PokemonResult
import com.gimnastiar.pokemon.domain.model.PokemonList
import com.gimnastiar.pokemon.domain.usecase.core.CoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: CoreUseCase
): ViewModel() {

    val getPokemon: Flow<PagingData<PokemonList>>
            = useCase.getPokemonList()
        .cachedIn(viewModelScope)
}