package com.cda.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val originLocationId: Int,
    val originLocationName: String,
    val lastKnownLocationId: Int,
    val lastKnownLocationName: String,
    val image: String,
    val episodeIds: List<Int>,
    val created: String,
)
