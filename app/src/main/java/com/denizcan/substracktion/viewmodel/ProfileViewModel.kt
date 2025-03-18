package com.denizcan.substracktion.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denizcan.substracktion.model.User
import com.denizcan.substracktion.repository.UserRepository
import com.denizcan.substracktion.util.CountryCurrencyManager
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.actionCodeSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false)

    private val _error = MutableStateFlow<String?>(null)

    private val _profilePhotoUrl = MutableStateFlow<String?>(null)

    private val auth = FirebaseAuth.getInstance()

    // Firebase auth provider kontrolü için
    private val _isPasswordProvider = MutableStateFlow(false)
    val isPasswordProvider = _isPasswordProvider.asStateFlow()

    init {
        loadUserProfile()

        // E-posta doğrulama durumunu ve provider'ı kontrol et
        auth.currentUser?.let { user ->
            val providers = user.providerData.map { it.providerId }
            _isPasswordProvider.value = providers.contains("password")
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

    fun deleteAccountWithPassword(
        password: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Mevcut kullanıcıyı al
                val user = auth.currentUser ?: return@launch
                
                // Kullanıcının email'ini al
                val email = user.email ?: return@launch
                
                // Kullanıcının kimlik bilgilerini yeniden doğrula
                val credential = EmailAuthProvider.getCredential(email, password)
                
                // Yeniden kimlik doğrulama
                user.reauthenticate(credential).await()
                
                // Firestore'dan kullanıcı verilerini sil
                userRepository.deleteAccount()
                
                // Firebase Storage'dan profil fotoğrafını sil
                user.photoUrl?.let { photoUrl ->
                    userRepository.deleteProfilePhoto(photoUrl)
                }
                
                // Firebase Auth'dan hesabı sil
                user.delete().await()
                
                // Başarılı callback'i çağır
                onSuccess()
                
            } catch (e: Exception) {
                // Hata durumunda error callback'i çağır
                onError()
                Log.e("ProfileViewModel", "Hesap silme hatası", e)
            }
        }
    }

    fun sendGoogleAccountDeletionEmail(onEmailSent: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            try {
                val user = auth.currentUser ?: return@launch
                val email = user.email ?: return@launch
                
                // Kullanıcının e-posta adresine doğrulama kodu gönder
                auth.sendSignInLinkToEmail(
                    email,
                    actionCodeSettings {
                        url = "substracktion.app/deleteAccount" // Deep link URL'iniz
                        handleCodeInApp = true
                        setAndroidPackageName(
                            "com.denizcan.substracktion",
                            true, // installIfNotAvailable
                            null // minimumVersion
                        )
                    }
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onEmailSent()
                    } else {
                        onError()
                    }
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Doğrulama e-postası gönderme hatası", e)
                onError()
            }
        }
    }
} 