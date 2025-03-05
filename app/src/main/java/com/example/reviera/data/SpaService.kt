package com.example.reviera.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "spa_services")
data class SpaService(
    @PrimaryKey(autoGenerate = true) val serviceId: Int = 0,
    val name: String,
    val description: String,
    val price: Double,
    val duration: Int,
    val isAvailable: Boolean
)