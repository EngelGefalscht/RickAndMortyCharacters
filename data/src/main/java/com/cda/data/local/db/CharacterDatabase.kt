package com.cda.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cda.data.local.dao.CharacterDao
import com.cda.data.local.dao.CharacterRemoteKeysDao
import com.cda.data.local.entity.CharacterEntity
import com.cda.data.local.entity.CharacterRemoteKeysEntity

@Database(
    entities = [
        CharacterEntity::class,
        CharacterRemoteKeysEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(RoomConverters::class)
abstract class CharacterDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun characterRemoteKeysDao(): CharacterRemoteKeysDao
}


