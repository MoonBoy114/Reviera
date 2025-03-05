package com.example.reviera

import android.app.Application
import androidx.room.Room
import com.example.reviera.data.SpaDatabase
import com.example.reviera.data.SpaRepository
import com.example.reviera.data.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SpaApplication : Application() {
    lateinit var database: SpaDatabase
    lateinit var repository: SpaRepository

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, SpaDatabase::class.java, "spa_database").build()
        repository = SpaRepository(database.userDao(), database.spaServiceDao(), database.bookingDao())

        // Добавление тестового пользователя
        CoroutineScope(Dispatchers.IO).launch {
            val testUser = User(
                email = "test@example.com",
                passwordHash = "123", // В реальном приложении используйте хэш
                firstName = "Test",
                lastName = "User",
                phone = "+1234567890",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            val existingUser = repository.login(testUser.email, testUser.passwordHash)
            if (existingUser == null) {
                repository.register(testUser)
            }
        }
    }
}