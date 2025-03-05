package com.example.reviera.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.reviera.data.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE email = :email AND passwordHash = :passwordHash LIMIT 1")
    suspend fun getUser(email: String, passwordHash: String): User?
}