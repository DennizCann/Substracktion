package com.denizcan.substracktion.repository

import android.net.Uri
import android.util.Log
import com.denizcan.substracktion.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.callbackFlow

class UserRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) {
    private val usersCollection = firestore.collection("users")

    // Kullanıcı profilini getir
    fun getUserProfile(): Flow<User?> = callbackFlow {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
        
        val listener = usersCollection.document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val user = snapshot?.toObject(User::class.java)
                trySend(user)
            }
            
        awaitClose { listener.remove() }
    }

    // Kullanıcı profilini güncelle
    suspend fun updateUserProfile(updates: Map<String, Any>): Result<Unit> = try {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
        
        usersCollection.document(userId)
            .update(updates)
            .await()
            
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    // Genel güncelleme fonksiyonu
    suspend fun updateUser(updates: Map<String, Any>): Result<Unit> = try {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
        
        usersCollection.document(userId)
            .update(updates)
            .await()
            
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    // Ülke güncelleme fonksiyonu
    suspend fun updateUserCountry(country: String): Result<Unit> = try {
        updateUser(mapOf("country" to country.uppercase()))
    } catch (e: Exception) {
        Result.failure(e)
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
                photoRef.putFile(uri).await()
                // Download URL'ini al ve döndür
                photoRef.downloadUrl.await().toString()
            } catch (e: Exception) {
                Log.e("UserRepository", "Profil fotoğrafı yükleme hatası", e)
                throw e
            }
        }
    }

    suspend fun deleteProfilePhoto(photoUrl: Uri) {
        try {
            // Firebase Storage referansını URL'den al
            val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(photoUrl.toString())
            // Fotoğrafı sil
            storageRef.delete().await()
        } catch (e: Exception) {
            Log.e("UserRepository", "Profil fotoğrafı silme hatası", e)
            throw e
        }
    }
} 