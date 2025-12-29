package com.cda.data.repository

import com.cda.data.remote.api.RickAndMortyApi
import com.cda.domain.model.Episode
import com.cda.domain.repository.EpisodeRepository
import java.time.Instant
import javax.inject.Inject

class EpisodeRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi,
) : EpisodeRepository {

    override suspend fun getEpisode(id: Int): Episode =
        api.getEpisode(id).toDomain()

    override suspend fun getEpisodes(ids: List<Int>): List<Episode> {
        if (ids.isEmpty()) return emptyList()
        return api.getEpisodes(ids.joinToString(",")).map { it.toDomain() }
    }
}

private fun com.cda.data.remote.dto.EpisodeDto.toDomain(): Episode =
    Episode(
        id = id,
        name = name,
        episode = episode,
        created = Instant.parse(created),
    )


