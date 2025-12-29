package com.cda.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cda.data.local.entity.CharacterRemoteKeysEntity

@Dao
interface CharacterRemoteKeysDao {

    @Query("SELECT * FROM character_remote_keys WHERE characterId = :characterId LIMIT 1")
    suspend fun remoteKeysByCharacterId(characterId: Int): CharacterRemoteKeysEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<CharacterRemoteKeysEntity>)

    @Query("DELETE FROM character_remote_keys")
    suspend fun clearRemoteKeys()
}


