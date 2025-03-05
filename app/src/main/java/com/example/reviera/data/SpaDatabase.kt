package com.example.reviera.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.reviera.data.dao.BookingDao
import com.example.reviera.data.dao.SpaServiceDao
import com.example.reviera.data.dao.UserDao

@Database(entities = [User::class, SpaService::class, Booking::class], version = 1)
abstract class SpaDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun spaServiceDao(): SpaServiceDao
    abstract fun bookingDao(): BookingDao
}