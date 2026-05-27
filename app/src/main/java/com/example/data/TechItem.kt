package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tech_items")
data class TechItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String, // "الذكاء الاصطناعي" | "تطوير الويب" | "الأمن السيبراني" | "التقنيات السحابية"
    val description: String,
    val launchYear: Int,
    val complexity: String, // "مبتدئ" | "متوسط" | "خبير"
    val importance: Int, // 1 to 5 stars
    val popularity: Int, // 1 to 100 popularity
    val isFavorite: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val logoName: String = "auto_awesome" // vector icons: "auto_awesome", "security", "web", "cloud", "code", "terminal", "dns", "wifi"
)
