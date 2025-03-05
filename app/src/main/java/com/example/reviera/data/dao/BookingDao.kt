package com.example.reviera.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.reviera.data.Booking
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    @Insert
    suspend fun insert(booking: Booking)

    @Query("SELECT * FROM bookings WHERE userId = :userId")
    fun getBookingsByUser(userId: Int): Flow<List<Booking>>
}