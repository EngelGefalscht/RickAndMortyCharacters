package com.cda.domain.repository

import com.cda.domain.model.Location

interface LocationRepository {
    suspend fun getLocation(id: Int): Location
    suspend fun getLocations(ids: List<Int>): List<Location>
}
