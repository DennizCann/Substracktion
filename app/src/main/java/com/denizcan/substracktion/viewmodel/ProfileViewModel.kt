package com.denizcan.substracktion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denizcan.substracktion.model.User
import com.denizcan.substracktion.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            userRepository.getUserProfile().collect { user ->
                if (user == null) {
                    createInitialProfile()
                } else {
                    _user.value = user
                }
            }
        }
    }

    private suspend fun createInitialProfile() {
        userRepository.createInitialProfile()
    }

    fun updateCountry(country: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                userRepository.updateUser(mapOf("country" to country))
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateCurrency(currencyCode: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                userRepository.updateUser(mapOf("currencyCode" to currencyCode))
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateNotifications(enabled: Boolean) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                userRepository.updateUser(mapOf("notificationsEnabled" to enabled))
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateEmailNotifications(enabled: Boolean) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                userRepository.updateUser(mapOf("emailNotifications" to enabled))
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateDisplayName(newName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                userRepository.updateUser(mapOf("displayName" to newName))
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
} 