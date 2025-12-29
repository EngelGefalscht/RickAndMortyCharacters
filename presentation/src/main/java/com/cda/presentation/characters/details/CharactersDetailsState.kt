package com.cda.presentation.characters.details

import com.cda.domain.model.Character
import com.cda.domain.model.Episode
import com.cda.domain.model.Location

data class CharactersDetailsState(
    val character: Character?,
    val origin: Location?,
    val lastKnown: Location?,
    val episodes: List<Episode>,
    val isLoading: Boolean,
    val error: String?,
) {
    companion object {
        fun initial(character: Character?): CharactersDetailsState =
            CharactersDetailsState(
                character = character,
                origin = null,
                lastKnown = null,
                episodes = emptyList(),
                isLoading = character != null,
                error = null,
            )
    }
}
