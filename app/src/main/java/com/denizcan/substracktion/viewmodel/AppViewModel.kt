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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

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

    private val auth = FirebaseAuth.getInstance()

    // E-posta doğrulama durumu
    private val _isEmailVerified = MutableStateFlow(false)

    // Doğrulama e-postası gönderme durumu
    private val _verificationEmailSent = MutableStateFlow(false)
    val verificationEmailSent = _verificationEmailSent.asStateFlow()

    init {
        // Başlangıçta mevcut kullanıcıyı kontrol edelim
        val currentUser = authRepository.currentUser
        println("DEBUG: Current user: ${currentUser?.email}, UID: ${currentUser?.uid}, Name: ${currentUser?.displayName}")
        _userName.value = currentUser?.displayName

        // Kullanıcının e-posta doğrulama durumunu kontrol et
        auth.currentUser?.let { user ->
            // Sadece e-posta/şifre ile giriş yapan kullanıcılar için kontrol et
            val providers = user.providerData.map { it.providerId }
            if (providers.contains("password")) {
                _isEmailVerified.value = user.isEmailVerified
            } else {
                // Google ile giriş yapan kullanıcılar için doğrulanmış kabul et
                _isEmailVerified.value = true
            }
        }
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


    fun signInWithEmail(email: String, password: String) = viewModelScope.launch {
        _isLoading.value = true
        _error.value = null
        _loginSuccess.value = false

        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.let { user ->
                // Önce e-posta doğrulama kontrolü yap
                if (!user.isEmailVerified) {
                    _error.value = if (language.value == Language.TURKISH)
                        "Lütfen e-posta adresinizi doğrulayın. Doğrulama bağlantısı gönderildi."
                    else
                        "Please verify your email address. Verification link has been sent."
                    // Yeni doğrulama e-postası gönder
                    sendVerificationEmail()
                    // Giriş başarısız
                    _loginSuccess.value = false
                    return@launch
                }

                // E-posta doğrulanmışsa giriş işlemine devam et
                user.reload()?.await()
                val userInfo = auth.currentUser
                println("DEBUG: Sign in successful - Email: ${userInfo?.email}, UID: ${userInfo?.uid}, Name: ${userInfo?.displayName}")
                _userName.value = userInfo?.displayName ?: "Kullanıcı"
                _isUserSignedIn.value = true
                _loginSuccess.value = true
            }
        } catch (e: Exception) {
            println("DEBUG: Sign in failed - ${e.message}")
            _error.value = e.message
            _loginSuccess.value = false
        } finally {
            _isLoading.value = false
        }
    }

    fun signInWithGoogle(idToken: String) = viewModelScope.launch {
        _isLoading.value = true
        _error.value = null
        _loginSuccess.value = false

        authRepository.signInWithGoogle(idToken)
            .onSuccess {
                // Google ile giriş yapan kullanıcılar için doğrulanmış kabul et
                _isEmailVerified.value = true
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
        _verificationEmailSent.value = false

        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let { user ->
                user.updateProfile(
                    UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                ).await()

                // Doğrulama e-postası gönder
                user.sendEmailVerification().await()

                // Otomatik giriş yapılmasını engelle
                auth.signOut()

                // Başarılı kayıt bildirimi
                _verificationEmailSent.value = true
            }
        } catch (e: Exception) {
            println("DEBUG: Registration failed - ${e.message}")
            _error.value = e.message
        } finally {
            _isLoading.value = false
        }
    }

    fun signOut() {
        authRepository.signOut()
        val user = authRepository.currentUser
        println("DEBUG: After sign out - User: ${user?.email}")
        _isUserSignedIn.value = false
        _userName.value = null
        // Çıkış yapınca state'i sıfırla
        _verificationEmailSent.value = false
    }

    // RegisterScreen'den giriş ekranına yönlendirildiğinde çağrılacak yeni fonksiyon
    fun clearVerificationState() {
        _verificationEmailSent.value = false
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

    fun clearResetPasswordSuccess() {
        _resetPasswordSuccess.value = false
    }

    fun updateLanguage(language: Language) {
        _language.value = language
        languageManager.saveLanguage(language)
    }

    fun saveFcmToken(token: String) {
        viewModelScope.launch {
            try {
                val currentUser = authRepository.currentUser ?: return@launch

                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(currentUser.uid)
                    .update("fcmToken", token)
                    .await()

                println("FCM token başarıyla kaydedildi: $token")
            } catch (e: Exception) {
                println("FCM token kaydedilirken hata oluştu: ${e.message}")
                _error.value = e.message
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

}