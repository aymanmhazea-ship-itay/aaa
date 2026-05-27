package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadDao {
    @Query("SELECT * FROM download_items ORDER BY timestamp DESC")
    fun getAllDownloads(): Flow<List<DownloadItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownload(item: DownloadItem): Long

    @Update
    suspend fun updateDownload(item: DownloadItem)

    @Query("DELETE FROM download_items WHERE id = :id")
    suspend fun deleteDownloadById(id: Int)

    @Query("DELETE FROM download_items")
    suspend fun deleteAllDownloads()
}
