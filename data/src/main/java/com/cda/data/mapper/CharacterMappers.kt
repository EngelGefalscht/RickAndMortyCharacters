package com.cda.data.mapper

import com.cda.data.local.entity.CharacterEntity
import com.cda.data.remote.dto.CharacterDto
import com.cda.domain.model.Character
import com.cda.domain.model.LocationRef
import java.time.Instant

internal fun CharacterDto.toEntity(): CharacterEntity =
    CharacterEntity(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        originLocationId = extractIdFromUrl(origin.url) ?: 0,
        originLocationName = origin.name,
        lastKnownLocationId = extractIdFromUrl(location.url) ?: 0,
        lastKnownLocationName = location.name,
        image = image,
        episodeIds = episode.mapNotNull(::extractIdFromUrl),
        created = created,
    )

internal fun CharacterDto.toDomain(): Character =
    Character(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        origin = LocationRef(
            id = extractIdFromUrl(origin.url) ?: 0,
            name = origin.name,
        ),
        lastKnownLocation = LocationRef(
            id = extractIdFromUrl(location.url) ?: 0,
            name = location.name,
        ),
        image = image,
        episodeIds = episode.mapNotNull(::extractIdFromUrl),
        created = Instant.parse(created),
    )

internal fun CharacterEntity.toDomain(): Character =
    Character(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        origin = LocationRef(id = originLocationId, name = originLocationName),
        lastKnownLocation = LocationRef(id = lastKnownLocationId, name = lastKnownLocationName),
        image = image,
        episodeIds = episodeIds,
        created = Instant.parse(created),
    )

