package com.example.reviera.data

import com.example.reviera.data.dao.BookingDao
import com.example.reviera.data.dao.SpaServiceDao
import com.example.reviera.data.dao.UserDao
import kotlinx.coroutines.flow.Flow

class SpaRepository(
    private val userDao: UserDao,
    private val spaServiceDao: SpaServiceDao,
    private val bookingDao: BookingDao
) {
    suspend fun login(email: String, passwordHash: String): User? = userDao.getUser(email, passwordHash)
    suspend fun register(user: User) = userDao.insert(user)
    fun getAllServices(): Flow<List<SpaService>> = spaServiceDao.getAllServices()
    suspend fun getServiceById(serviceId: Int): SpaService? = spaServiceDao.getServiceById(serviceId)
    suspend fun bookService(booking: Booking) = bookingDao.insert(booking)
    fun getBookingsByUser(userId: Int): Flow<List<Booking>> = bookingDao.getBookingsByUser(userId)
}