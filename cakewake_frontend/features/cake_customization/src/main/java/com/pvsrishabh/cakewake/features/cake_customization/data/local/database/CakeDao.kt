package com.pvsrishabh.cakewake.features.cake_customization.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CakeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCake(cake: CakeEntity)

    @Query("SELECT * FROM cake_customizations WHERE id = :id")
    suspend fun getCakeById(id: String): CakeEntity?

    @Query("SELECT * FROM cake_customizations")
    suspend fun getAllCakes(): List<CakeEntity>

    @Query("DELETE FROM cake_customizations WHERE id = :id")
    suspend fun deleteCakeById(id: String)

    @Query("SELECT * FROM cake_customizations WHERE id LIKE '%' || :query || '%'")
    suspend fun searchCakes(query: String): List<CakeEntity>

    @Query("SELECT * FROM cake_customizations ORDER BY createdAt DESC LIMIT :limit")
    suspend fun getRecentCakes(limit: Int): List<CakeEntity>
}