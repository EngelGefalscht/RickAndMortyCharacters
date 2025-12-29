package com.cda.presentation.characters.navigation

import com.cda.domain.model.Character
import com.cda.domain.model.LocationRef
import kotlinx.serialization.Serializable
import java.time.Instant

sealed interface CharactersDestination {

    @Serializable
    data object List : CharactersDestination

    @Serializable
    data class Details(val character: CharacterNavArg) : CharactersDestination
}

@Serializable
data class CharacterNavArg(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val originId: Int,
    val originName: String,
    val lastKnownId: Int,
    val lastKnownName: String,
    val image: String,
    val episodeIds: List<Int>,
    val createdIso: String,
)

fun Character.toNavArg(): CharacterNavArg =
    CharacterNavArg(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        originId = origin.id,
        originName = origin.name,
        lastKnownId = lastKnownLocation.id,
        lastKnownName = lastKnownLocation.name,
        image = image,
        episodeIds = episodeIds,
        createdIso = created.toString(),
    )

fun CharacterNavArg.toDomain(): Character =
    Character(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        origin = LocationRef(id = originId, name = originName),
        lastKnownLocation = LocationRef(id = lastKnownId, name = lastKnownName),
        image = image,
        episodeIds = episodeIds,
        created = Instant.parse(createdIso),
    )

