package com.cda.presentation.characters.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cda.characters_paging.CharacterPagedRepository
import com.cda.domain.model.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CharactersListUiState(
    val query: String = "",
)

sealed interface CharactersListIntent {
    data class QueryChanged(val value: String) : CharactersListIntent
    data object ClearQuery : CharactersListIntent
}

@HiltViewModel
class CharactersListViewModel @Inject constructor(
    private val repository: CharacterPagedRepository,
) : ViewModel() {
    private val intents = MutableSharedFlow<CharactersListIntent>(extraBufferCapacity = 32)

    private val _uiState = MutableStateFlow(CharactersListUiState())
    val uiState =
        _uiState.stateIn(viewModelScope, SharingStarted.Eagerly, _uiState.value)

    @OptIn(ExperimentalCoroutinesApi::class)
    val characters: Flow<PagingData<Character>> =
        _uiState
            .map { it.query.trim() }
            .distinctUntilChanged()
            .flatMapLatest { q ->
                if (q.isBlank()) repository.characters else repository.search(q)
            }
            .cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            intents.collect { intent ->
                when (intent) {
                    is CharactersListIntent.QueryChanged -> _uiState.update { it.copy(query = intent.value) }
                    CharactersListIntent.ClearQuery -> _uiState.update { it.copy(query = "") }
                }
            }
        }
    }

    fun accept(intent: CharactersListIntent) {
        intents.tryEmit(intent)
    }
}
