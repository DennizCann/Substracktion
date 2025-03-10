package com.denizcan.substracktion.util

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


fun DocumentReference.snapshotFlow(): Flow<DocumentSnapshot> = callbackFlow {
    val listenerRegistration = addSnapshotListener { value, error ->
        if (error != null) {
            close(error)
            return@addSnapshotListener
        }
        if (value != null) {
            trySend(value)
        }
    }
    awaitClose { listenerRegistration.remove() }
} 