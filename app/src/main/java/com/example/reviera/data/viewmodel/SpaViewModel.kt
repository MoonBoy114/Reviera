package com.example.reviera.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.reviera.data.SpaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Promotion(val title: String, val description: String)

class SpaViewModel(private val repository: SpaRepository) : ViewModel() {
    private val _promotions = MutableStateFlow<List<Promotion>>(emptyList())
    val promotions: StateFlow<List<Promotion>> = _promotions

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    fun loadPromotions() {
        viewModelScope.launch {
            _promotions.value = listOf(
                Promotion("Продлите свой отдых со скидкой 15%", "Ограничено по времени")
            )
        }
    }

    fun login(email: String, passwordHash: String) {
        viewModelScope.launch {
            val user = repository.login(email, passwordHash)
            _isLoggedIn.value = user != null
        }
    }

    fun isUserLoggedIn(): Boolean = _isLoggedIn.value
}

class SpaViewModelFactory(private val repository: SpaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SpaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SpaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}