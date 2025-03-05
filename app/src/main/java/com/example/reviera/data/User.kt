package com.example.reviera.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,
    val email: String,
    val passwordHash: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val createdAt: Long,
    val updatedAt: Long
)