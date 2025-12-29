package com.cda.characters_paging

import androidx.paging.PagingData
import com.cda.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterPagedRepository {
    val characters: Flow<PagingData<Character>>

    fun search(name: String): Flow<PagingData<Character>>
}
