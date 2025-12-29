package com.cda.data.local.db

import androidx.room.TypeConverter

/**
 * Stores episode urls list as a single string.
 * Note: URLs won't contain the separator in practice, but keep it consistent anyway.
 */
class RoomConverters {
    private val separator = "|"

    @TypeConverter
    fun fromEpisodeIds(value: List<Int>): String = value.joinToString(separator = separator)

    @TypeConverter
    fun toEpisodeIds(value: String): List<Int> =
        if (value.isBlank()) emptyList() else value.split(separator).mapNotNull { it.toIntOrNull() }
}


