package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TechDao {
    @Query("SELECT * FROM tech_items ORDER BY timestamp DESC")
    fun getAllTechItems(): Flow<List<TechItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTechItem(item: TechItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTechItems(items: List<TechItem>)

    @Query("UPDATE tech_items SET isFavorite = :isFav WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Int, isFav: Boolean)

    @Query("DELETE FROM tech_items WHERE id = :id")
    suspend fun deleteTechItemById(id: Int)

    @Query("SELECT COUNT(*) FROM tech_items")
    suspend fun getCount(): Int
}
