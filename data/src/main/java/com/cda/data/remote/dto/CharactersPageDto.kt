package com.cda.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CharactersPageDto(
    @field:Json(name = "info") val info: PageInfoDto,
    @field:Json(name = "results") val characters: List<CharacterDto>,
)

@JsonClass(generateAdapter = true)
data class PageInfoDto(
    @field:Json(name = "count") val count: Int,
    @field:Json(name = "pages") val pages: Int,
    @field:Json(name = "next") val next: String?,
    @field:Json(name = "prev") val prev: String?,
)

