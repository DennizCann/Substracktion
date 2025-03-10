package com.denizcan.substracktion.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AuthRepository {
    val auth: FirebaseAuth = Firebase.auth
    val currentUser = auth.currentUser


    suspend fun signInWithGoogle(idToken: String): Result<Unit> = try {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).await()
        auth.currentUser?.reload()?.await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }


    fun signOut() {
        auth.signOut()
    }

    suspend fun resetPassword(email: String): Result<Unit> = try {
        auth.sendPasswordResetEmail(email).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
} 