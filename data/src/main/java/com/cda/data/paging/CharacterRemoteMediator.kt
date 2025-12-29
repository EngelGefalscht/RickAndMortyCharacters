package com.cda.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.cda.data.datasource.CharacterLocalDataSource
import com.cda.data.datasource.CharacterRemoteDataSource
import com.cda.data.local.entity.CharacterEntity
import com.cda.data.mapper.toEntity
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator @Inject constructor(
    private val remote: CharacterRemoteDataSource,
    private val local: CharacterLocalDataSource,
) : RemoteMediator<Int, CharacterEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>,
    ): MediatorResult {
        return when (loadType) {
            LoadType.REFRESH -> {
                fetchAndStorePage(page = 1, isRefresh = true)
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                } else {
                    fetchAndStorePage(page = prevKey, isRefresh = false)
                }
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                } else {
                    fetchAndStorePage(page = nextKey, isRefresh = false)
                }
            }
        }
    }

    private suspend fun fetchAndStorePage(page: Int, isRefresh: Boolean): MediatorResult {
        return try {
            val response = remote.getCharacters(page)
            val items = response.characters.map { it.toEntity() }
            val endOfPaginationReached = response.info.next == null
            val prevKey = if (page == 1) null else page - 1
            val nextKey = if (endOfPaginationReached) null else page + 1

            local.insertPage(
                items = items,
                prevKey = prevKey,
                nextKey = nextKey,
                isRefresh = isRefresh,
            )
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, CharacterEntity>,
    ) = state.pages.lastOrNull { it.data.isNotEmpty() }
        ?.data
        ?.lastOrNull()
        ?.let { local.remoteKeysByCharacterId(it.id) }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, CharacterEntity>,
    ) = state.pages.firstOrNull { it.data.isNotEmpty() }
        ?.data
        ?.firstOrNull()
        ?.let { local.remoteKeysByCharacterId(it.id) }

}
