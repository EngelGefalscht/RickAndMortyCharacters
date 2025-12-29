package com.cda.domain.model

import java.io.Serializable

data class Location(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
): Serializable
