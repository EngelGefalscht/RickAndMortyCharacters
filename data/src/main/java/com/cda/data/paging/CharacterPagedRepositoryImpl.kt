package com.cda.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.cda.characters_paging.CharacterPagedRepository
import com.cda.data.datasource.CharacterLocalDataSource
import com.cda.data.mapper.toDomain
import com.cda.data.remote.api.RickAndMortyApi
import com.cda.domain.model.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterPagedRepositoryImpl @Inject constructor(
    private val localSource: CharacterLocalDataSource,
    private val remoteMediator: CharacterRemoteMediator,
    private val api: RickAndMortyApi,
) : CharacterPagedRepository {

    @OptIn(ExperimentalPagingApi::class)
    private val pager = Pager(
        config = PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
            prefetchDistance = 5,
            enablePlaceholders = false,
        ),
        remoteMediator = remoteMediator,
        pagingSourceFactory = { localSource.pagingSource() },
    )

    @OptIn(ExperimentalPagingApi::class)
    override val characters: Flow<PagingData<Character>> =
        pager
            .flow
            .map { pagingData -> pagingData.map { it.toDomain() } }

    override fun search(name: String): Flow<PagingData<Character>> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { CharactersSearchPagingSource(api = api, name = name) },
        ).flow
}
