package com.pvsrishabh.cakewake.features.cake_customization.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

// Room Database
@Database(
    entities = [CakeEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CakeDatabase : RoomDatabase() {
    abstract val cakeDao: CakeDao
}