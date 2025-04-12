package com.gimnastiar.pokemon.ui.screen.core.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gimnastiar.pokemon.domain.model.Pokemon
import com.gimnastiar.pokemon.domain.model.PokemonList
import com.gimnastiar.pokemon.domain.usecase.core.CoreUseCase
import com.gimnastiar.pokemon.utils.NetworkConnectionLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: CoreUseCase
): ViewModel() {

    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean> get() = _isConnected

    private val _searchQuery = MutableStateFlow("")
    private val searchQuery: StateFlow<String> = _searchQuery

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val getPokemonList = searchQuery
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            useCase.getPokemonListSearch(query)
        }
        .cachedIn(viewModelScope)

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun checkConnectingData(context: Context, lifecycleOwner: LifecycleOwner) {
        NetworkConnectionLiveData(context).observe(lifecycleOwner) { isOnline ->
            _isConnected.value = isOnline
        }
    }

    val getPokemon: Flow<PagingData<PokemonList>>
            = useCase.getPokemonList()
        .cachedIn(viewModelScope)

    val getLocalPokemon: Flow<List<Pokemon>>
    = useCase.getAllPokemonFlow()
}