package com.example.reviera.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.reviera.data.SpaService
import kotlinx.coroutines.flow.Flow

@Dao
interface SpaServiceDao {
    @Query("SELECT * FROM spa_services")
    fun getAllServices(): Flow<List<SpaService>>

    @Query("SELECT * FROM spa_services WHERE serviceId = :serviceId")
    suspend fun getServiceById(serviceId: Int): SpaService?
}