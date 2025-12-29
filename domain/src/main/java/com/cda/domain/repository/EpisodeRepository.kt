package com.cda.domain.repository

import com.cda.domain.model.Episode

interface EpisodeRepository {
    suspend fun getEpisode(id: Int): Episode
    suspend fun getEpisodes(ids: List<Int>): List<Episode>
}
