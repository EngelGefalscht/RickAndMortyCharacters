package com.cda.data.repository

import com.cda.data.remote.api.RickAndMortyApi
import com.cda.domain.model.Location
import com.cda.domain.repository.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi,
) : LocationRepository {

    override suspend fun getLocation(id: Int): Location =
        api.getLocation(id).toDomain()

    override suspend fun getLocations(ids: List<Int>): List<Location> {
        if (ids.isEmpty()) return emptyList()
        return api.getLocations(ids.joinToString(",")).map { it.toDomain() }
    }
}

private fun com.cda.data.remote.dto.LocationDto.toDomain(): Location =
    Location(
        id = id,
        name = name,
        type = type,
        dimension = dimension,
    )


