package com.denizcan.substracktion.util

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

// Döküman için snapshot flow
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

// Koleksiyon için snapshot flow
fun CollectionReference.snapshotFlow(): Flow<QuerySnapshot> = callbackFlow {
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