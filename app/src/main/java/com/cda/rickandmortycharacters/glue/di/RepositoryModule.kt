package com.cda.rickandmortycharacters.glue.di

import com.cda.characters_paging.CharacterPagedRepository
import com.cda.data.paging.CharacterPagedRepositoryImpl
import com.cda.data.repository.EpisodeRepositoryImpl
import com.cda.data.repository.LocationRepositoryImpl
import com.cda.domain.repository.EpisodeRepository
import com.cda.domain.repository.LocationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun charactersPagedRepository(impl: CharacterPagedRepositoryImpl): CharacterPagedRepository

    @Binds
    @Singleton
    fun locationRepository(impl: LocationRepositoryImpl): LocationRepository

    @Binds
    @Singleton
    fun episodeRepository(impl: EpisodeRepositoryImpl): EpisodeRepository

}
