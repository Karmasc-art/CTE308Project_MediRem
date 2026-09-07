package com.example.medirem.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicines")
data class Medicine(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val dosage: String,
    val note: String = "",
    val time: String,
    val date: String,
    val photoUri: String? = null,
    val taken: Boolean = false
)