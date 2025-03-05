package com.example.reviera.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey(autoGenerate = true) val bookingId: Int = 0,
    val userId: Int,
    val serviceId: Int,
    val bookingDate: Long,
    val status: String,
    val createdAt: Long
)