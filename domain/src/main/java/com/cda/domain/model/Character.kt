package com.cda.domain.model

import java.io.Serializable
import java.time.Instant

data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: LocationRef,
    val lastKnownLocation: LocationRef,
    val image: String,
    val episodeIds: List<Int>,
    val created: Instant,
): Serializable

data class LocationRef(
    val id: Int,
    val name: String,
): Serializable
