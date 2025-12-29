package com.cda.data.remote.api

import com.cda.data.remote.dto.CharacterDto
import com.cda.data.remote.dto.CharactersPageDto
import com.cda.data.remote.dto.EpisodeDto
import com.cda.data.remote.dto.LocationDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int? = null,
        @Query("name") name: String? = null,
        @Query("status") status: String? = null,
        @Query("species") species: String? = null,
        @Query("type") type: String? = null,
        @Query("gender") gender: String? = null,
    ): CharactersPageDto

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): CharacterDto

    @GET("location/{id}")
    suspend fun getLocation(@Path("id") id: Int): LocationDto

    // /location/1,2,3
    @GET("location/{ids}")
    suspend fun getLocations(@Path("ids") ids: String): List<LocationDto>

    @GET("episode/{id}")
    suspend fun getEpisode(@Path("id") id: Int): EpisodeDto

    // /episode/1,2,3
    @GET("episode/{ids}")
    suspend fun getEpisodes(@Path("ids") ids: String): List<EpisodeDto>
}


