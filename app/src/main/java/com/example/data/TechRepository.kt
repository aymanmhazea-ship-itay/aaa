package com.example.data

import kotlinx.coroutines.flow.Flow

class TechRepository(private val techDao: TechDao) {
    val allTechItems: Flow<List<TechItem>> = techDao.getAllTechItems()

    suspend fun insert(item: TechItem) {
        techDao.insertTechItem(item)
    }

    suspend fun insertAll(items: List<TechItem>) {
        techDao.insertTechItems(items)
    }

    suspend fun updateFavorite(id: Int, isFav: Boolean) {
        techDao.updateFavoriteStatus(id, isFav)
    }

    suspend fun delete(id: Int) {
        techDao.deleteTechItemById(id)
    }

    suspend fun getCount(): Int {
        return techDao.getCount()
    }
}
