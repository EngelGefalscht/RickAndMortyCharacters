package com.cda.data.datasource

import com.cda.data.remote.api.RickAndMortyApi
import com.cda.data.remote.dto.CharactersPageDto
import javax.inject.Inject

class CharacterRemoteDataSource @Inject constructor(
    private val api: RickAndMortyApi,
) {
    suspend fun getCharacters(page: Int): CharactersPageDto =
        api.getCharacters(page = page)
}
