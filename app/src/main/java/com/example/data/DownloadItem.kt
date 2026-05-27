package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "download_items")
data class DownloadItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val url: String,
    val title: String,
    val filename: String?,
    val mediaType: String, // "فيديو" | "صوت" | "صورة"
    val quality: String?,
    val duration: String?,
    val status: String, // "مكتمل" | "جاري التحميل" | "فشل"
    val progress: Float = 1.0f,
    val timestamp: Long = System.currentTimeMillis()
)
