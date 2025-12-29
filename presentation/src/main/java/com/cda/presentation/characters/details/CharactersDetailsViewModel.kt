package com.cda.presentation.characters.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cda.domain.model.Character
import com.cda.domain.repository.EpisodeRepository
import com.cda.domain.repository.LocationRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = CharactersDetailsViewModel.Factory::class)
class CharactersDetailsViewModel @AssistedInject constructor(
    private val locationRepository: LocationRepository,
    private val episodeRepository: EpisodeRepository,
    @Assisted val character: Character,
) : ViewModel() {

    private val _state = MutableStateFlow(CharactersDetailsState.initial(character))
    val state: StateFlow<CharactersDetailsState> = _state.asStateFlow()

    init {
        loadDetails(character)
    }

    private fun loadDetails(character: Character) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val originDeferred = async { locationRepository.getLocation(character.origin.id) }
                val lastDeferred = async { locationRepository.getLocation(character.lastKnownLocation.id) }
                val episodesDeferred = async { episodeRepository.getEpisodes(character.episodeIds.distinct()) }

                val origin = originDeferred.await()
                val last = lastDeferred.await()
                val episodes = episodesDeferred.await()

                _state.update {
                    it.copy(
                        character = character,
                        origin = origin,
                        lastKnown = last,
                        episodes = episodes,
                        isLoading = false,
                        error = null,
                    )
                }
            } catch (t: Throwable) {
                _state.update { it.copy(isLoading = false, error = t.message ?: "Unknown error") }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(character: Character): CharactersDetailsViewModel
    }
}


