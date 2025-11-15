package com.receparslan.artbook.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "arts")
data class Art(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String,
    val artist: String,
    val year: String,
    val imageUrl: String
)