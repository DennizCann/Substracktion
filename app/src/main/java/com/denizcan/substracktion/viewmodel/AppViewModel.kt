package com.denizcan.substracktion.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.denizcan.substracktion.R
import com.denizcan.substracktion.repository.AuthRepository
import com.denizcan.substracktion.util.Language
import com.denizcan.substracktion.util.LanguageManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val languageManager = LanguageManager(application)
    
    private val _language = mutableStateOf(languageManager.getLanguage())
    val language: State<Language> = _language

    val authRepository = AuthRepository()
    private var googleSignInClient: GoogleSignInClient? = null

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _isUserSignedIn = MutableStateFlow(authRepository.currentUser != null)
    val isUserSignedIn = _isUserSignedIn.asStateFlow()

    private val _userName = MutableStateFlow<String?>(authRepository.currentUser?.displayName)
    val userName = _userName.asStateFlow()

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess = _loginSuccess.asStateFlow()

    private val _resetPasswordSuccess = MutableStateFlow(false)
    val resetPasswordSuccess = _resetPasswordSuccess.asStateFlow()

    init {
        // Başlangıçta mevcut kullanıcıyı kontrol edelim
        val currentUser = authRepository.currentUser
        println("DEBUG: Current user: ${currentUser?.email}, UID: ${currentUser?.uid}, Name: ${currentUser?.displayName}")
        _userName.value = currentUser?.displayName
    }

    fun initGoogleSignIn(context: Context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.web_client_id))
            .requestEmail()
            .build()
        
        // Her seferinde yeni bir oturum başlat
        googleSignInClient = GoogleSignIn.getClient(context, gso)
        // Mevcut oturumu kapat
        googleSignInClient?.signOut()
        
        println("DEBUG: Google Sign In initialized with web client ID: ${context.getString(R.string.web_client_id)}")
    }

    // Google Sign-In butonuna tıklandığında çağrılacak fonksiyon
    fun prepareGoogleSignIn() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Mevcut Google oturumunu kapat
                googleSignInClient?.signOut()?.await()
                // Hesap seçiciyi sıfırla
                googleSignInClient?.revokeAccess()?.await()
            } catch (e: Exception) {
                println("DEBUG: Failed to prepare Google Sign In: ${e.message}")
                // Hata olsa bile devam et
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setLanguage(newLanguage: Language) {
        _language.value = newLanguage
        languageManager.saveLanguage(newLanguage)
    }

    fun signInWithEmail(email: String, password: String) = viewModelScope.launch {
        _isLoading.value = true
        _error.value = null
        _loginSuccess.value = false

        authRepository.signInWithEmail(email, password)
            .onSuccess {
                // Kullanıcı bilgilerini yenile
                authRepository.auth.currentUser?.reload()?.await()
                val user = authRepository.auth.currentUser
                println("DEBUG: Sign in successful - Email: ${user?.email}, UID: ${user?.uid}, Name: ${user?.displayName}")
                _userName.value = user?.displayName ?: "Kullanıcı"  // userName'i güncelle
                _isUserSignedIn.value = true
                _loginSuccess.value = true
            }
            .onFailure { e ->
                println("DEBUG: Sign in failed - ${e.message}")
                _error.value = e.message
                _loginSuccess.value = false
            }

        _isLoading.value = false
    }

    fun signInWithGoogle(idToken: String) = viewModelScope.launch {
        _isLoading.value = true
        _error.value = null
        _loginSuccess.value = false

        authRepository.signInWithGoogle(idToken)
            .onSuccess {
                // Kullanıcı bilgilerini yenile
                authRepository.auth.currentUser?.reload()?.await()
                val user = authRepository.auth.currentUser
                println("DEBUG: Google sign in successful - Email: ${user?.email}, UID: ${user?.uid}, Name: ${user?.displayName}")
                _userName.value = user?.displayName ?: "Kullanıcı"
                _isUserSignedIn.value = true
                _loginSuccess.value = true
            }
            .onFailure { e ->
                println("DEBUG: Google sign in failed - ${e.message}")
                _error.value = e.message
                _loginSuccess.value = false
            }

        _isLoading.value = false
    }

    fun register(name: String, email: String, password: String) = viewModelScope.launch {
        _isLoading.value = true
        _error.value = null
        _loginSuccess.value = false

        authRepository.register(name, email, password)
            .onSuccess {
                // Kullanıcı bilgilerini yenile
                authRepository.auth.currentUser?.reload()?.await()
                // Güncel kullanıcı bilgisini al
                val user = authRepository.auth.currentUser
                println("DEBUG: Registration successful - Email: ${user?.email}, UID: ${user?.uid}, Name: ${user?.displayName}")
                _userName.value = user?.displayName ?: name  // Önce Firebase'den al, yoksa girilen ismi kullan
                _isUserSignedIn.value = true
                _loginSuccess.value = true
            }
            .onFailure { e ->
                println("DEBUG: Registration failed - ${e.message}")
                _error.value = e.message
                _loginSuccess.value = false
            }

        _isLoading.value = false
    }

    fun signOut() {
        authRepository.signOut()
        val user = authRepository.currentUser
        println("DEBUG: After sign out - User: ${user?.email}")
        _isUserSignedIn.value = false
        _userName.value = null
    }

    fun getGoogleSignInIntent() = googleSignInClient?.signInIntent

    fun setError(message: String?) {
        _error.value = message
    }

    fun resetPassword(email: String) = viewModelScope.launch {
        _isLoading.value = true
        _error.value = null
        _resetPasswordSuccess.value = false
        
        authRepository.resetPassword(email)
            .onSuccess {
                println("DEBUG: Password reset email sent to $email")
                _resetPasswordSuccess.value = true
            }
            .onFailure { e ->
                println("DEBUG: Password reset failed - ${e.message}")
                _error.value = e.message
            }
        
        _isLoading.value = false
    }
} 