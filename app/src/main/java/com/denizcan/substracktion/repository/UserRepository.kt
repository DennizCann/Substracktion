package com.denizcan.substracktion.repository

import android.net.Uri
import com.denizcan.substracktion.model.User
import com.denizcan.substracktion.util.snapshotFlow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepository(
    val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) {
    private val usersCollection = firestore.collection("users")


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

    suspend fun deleteAccount() {
        try {
            // Önce Firestore'daki kullanıcı verilerini sil
            val userId = auth.currentUser?.uid ?: throw Exception("User not found")
            firestore.collection("users").document(userId).delete().await()
            
            // Sonra Firebase Auth'daki hesabı sil
            auth.currentUser?.delete()?.await()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun uploadProfilePhoto(uri: Uri): String {
        val storage = FirebaseStorage.getInstance()
        val userId = auth.currentUser?.uid ?: throw Exception("User not found")
        val photoRef = storage.reference.child("profile_photos/$userId.jpg")

        return withContext(Dispatchers.IO) {
            try {
                // Fotoğrafı yükle
                // Download URL'ini al
                photoRef.downloadUrl.await().toString()
            } catch (e: Exception) {
                throw e
            }
        }
    }
} 