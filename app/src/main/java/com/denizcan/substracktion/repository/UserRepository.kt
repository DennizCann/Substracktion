package com.denizcan.substracktion.repository

import android.content.Context
import com.denizcan.substracktion.model.User
import com.denizcan.substracktion.util.snapshotFlow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val context: Context
) {
    private val prefs = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    private val usersCollection = firestore.collection("users")

    fun getDarkModePreference(): Flow<Boolean> = flow {
        emit(prefs.getBoolean("dark_mode", false))
    }

    suspend fun updateDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean("dark_mode", enabled).apply()
        
        // Firestore'a da kaydedelim
        val currentUser = auth.currentUser ?: return
        usersCollection.document(currentUser.uid)
            .update("darkMode", enabled)
            .await()
    }

    // Kullanıcı profili oluştur
    suspend fun createUserProfile(
        displayName: String,
        country: String = "TR",
        currencyCode: String = "TRY"
    ) {
        val currentUser = auth.currentUser ?: return

        val user = User(
            id = currentUser.uid,
            email = currentUser.email ?: "",
            displayName = displayName,
            country = country,
            currencyCode = currencyCode
        )

        usersCollection.document(user.id)
            .set(user)
            .await()
    }

    // Kullanıcı profilini getir - performans iyileştirmesi
    fun getUserProfile(): Flow<User?> {
        val currentUser = auth.currentUser ?: return flowOf(null)
        
        return usersCollection.document(currentUser.uid)
            .snapshotFlow()
            .map { snapshot -> snapshot.toObject(User::class.java) }
            .catch { emit(null) }
    }

    // Kullanıcı bilgilerini güncelle - performans iyileştirmesi
    suspend fun updateUser(updates: Map<String, Any>) {
        try {
            val currentUser = auth.currentUser ?: return
            usersCollection.document(currentUser.uid)
                .update(updates)
                .await()
        } catch (e: Exception) {
            // Hata durumunda sessizce devam et
        }
    }

    // İlk profili oluştur - performans iyileştirmesi
    suspend fun createInitialProfile() {
        try {
            val currentUser = auth.currentUser ?: return
            
            val user = User(
                id = currentUser.uid,
                email = currentUser.email ?: "",
                displayName = currentUser.displayName ?: ""
            )
            
            usersCollection.document(currentUser.uid)
                .set(user)
                .await()
        } catch (e: Exception) {
            // Hata durumunda sessizce devam et
        }
    }
} 