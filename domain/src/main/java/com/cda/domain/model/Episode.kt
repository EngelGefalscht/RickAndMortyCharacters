package com.cda.domain.model

import java.io.Serializable
import java.time.Instant

data class Episode(
    val id: Int,
    val name: String,
    val episode: String,
    val created: Instant,
): Serializable
