package com.cda.data.datasource

import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.cda.data.local.dao.CharacterDao
import com.cda.data.local.dao.CharacterRemoteKeysDao
import com.cda.data.local.db.CharacterDatabase
import com.cda.data.local.entity.CharacterEntity
import com.cda.data.local.entity.CharacterRemoteKeysEntity
import javax.inject.Inject

class CharacterLocalDataSource @Inject constructor(
    private val db: CharacterDatabase,
) {
    private val characterDao: CharacterDao = db.characterDao()
    private val remoteKeysDao: CharacterRemoteKeysDao = db.characterRemoteKeysDao()

    fun pagingSource(): PagingSource<Int, CharacterEntity> = characterDao.pagingSource()

    suspend fun remoteKeysByCharacterId(characterId: Int): CharacterRemoteKeysEntity? =
        remoteKeysDao.remoteKeysByCharacterId(characterId)

    suspend fun insertPage(
        items: List<CharacterEntity>,
        prevKey: Int?,
        nextKey: Int?,
        isRefresh: Boolean,
    ) {
        db.withTransaction {
            if (isRefresh) {
                remoteKeysDao.clearRemoteKeys()
                characterDao.clearAll()
            }

            val keys = items.map {
                CharacterRemoteKeysEntity(
                    characterId = it.id,
                    prevKey = prevKey,
                    nextKey = nextKey,
                )
            }
            remoteKeysDao.insertAll(keys)
            characterDao.upsertAll(items)
        }
    }

    suspend fun clearAll() {
        db.withTransaction {
            remoteKeysDao.clearRemoteKeys()
            characterDao.clearAll()
        }
    }
}
