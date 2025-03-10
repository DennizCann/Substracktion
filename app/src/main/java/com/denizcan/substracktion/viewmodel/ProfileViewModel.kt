package com.denizcan.substracktion.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denizcan.substracktion.model.User
import com.denizcan.substracktion.repository.UserRepository
import com.denizcan.substracktion.util.CountryCurrencyManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false)

    private val _error = MutableStateFlow<String?>(null)

    private val _profilePhotoUrl = MutableStateFlow<String?>(null)

    private val auth = FirebaseAuth.getInstance()

    // E-posta doğrulama durumu
    private val _isEmailVerified = MutableStateFlow(false)
    val isEmailVerified = _isEmailVerified.asStateFlow()

    // Doğrulama e-postası gönderme durumu
    private val _verificationEmailSent = MutableStateFlow(false)
    val verificationEmailSent = _verificationEmailSent.asStateFlow()

    // Firebase auth provider kontrolü için
    private val _isPasswordProvider = MutableStateFlow(false)
    val isPasswordProvider = _isPasswordProvider.asStateFlow()

    init {
        loadUserProfile()

        // E-posta doğrulama durumunu ve provider'ı kontrol et
        auth.currentUser?.let { user ->
            val providers = user.providerData.map { it.providerId }
            _isPasswordProvider.value = providers.contains("password")
            
            if (_isPasswordProvider.value) {
                _isEmailVerified.value = user.isEmailVerified
            } else {
                // Google ile giriş yapan kullanıcılar için doğrulanmış kabul et
                _isEmailVerified.value = true
            }
        }
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

    fun updateCountry(countryCode: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Ülke değiştiğinde para birimini de otomatik güncelle
                val currencyCode = CountryCurrencyManager.getCurrencyForCountry(countryCode)
                userRepository.updateUser(
                    mapOf(
                        "country" to countryCode,
                        "currencyCode" to currencyCode
                    )
                )
                _user.value = _user.value?.copy(
                    country = countryCode,
                    currencyCode = currencyCode
                )
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

    fun deleteAccount(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                userRepository.deleteAccount()
                _error.value = null
                onSuccess()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun uploadProfilePhoto(uri: Uri) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val photoUrl = userRepository.uploadProfilePhoto(uri)
                _profilePhotoUrl.value = photoUrl
                userRepository.updateUser(mapOf("photoUrl" to photoUrl))
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Doğrulama e-postası gönder
    fun sendVerificationEmail() {
        auth.currentUser?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _verificationEmailSent.value = true
            }
        }
    }

    // E-posta doğrulama durumunu yenile
    fun refreshEmailVerificationStatus() {
        auth.currentUser?.reload()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _isEmailVerified.value = auth.currentUser?.isEmailVerified ?: false
            }
        }
    }
} 