package com.denizcan.substracktion.repository

import com.denizcan.substracktion.model.*
import com.denizcan.substracktion.util.snapshotFlow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class SubscriptionRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    private val servicesCollection = firestore.collection("services")
    private val userSubscriptionsCollection = firestore.collection("user_subscriptions")

    // Tüm servisleri getir
    fun getServices(): Flow<List<Service>> {
        return servicesCollection
            .snapshotFlow()
            .map { snapshot: QuerySnapshot -> 
                snapshot.documents.mapNotNull { doc -> 
                    doc.toObject(Service::class.java)?.copy(id = doc.id)
                }
            }
            .catch { emit(emptyList()) }
    }

    // Belirli bir servisin planlarını getir
    fun getServicePlans(serviceId: String): Flow<List<ServicePlan>> {
        return servicesCollection
            .document(serviceId)
            .collection("plans")
            .snapshotFlow()
            .map { snapshot: QuerySnapshot ->
                snapshot.documents.mapNotNull { doc ->
                    doc.toObject(ServicePlan::class.java)?.copy(id = doc.id)
                }
            }
            .catch { emit(emptyList()) }
    }

    // Kullanıcının üyeliklerini getir
    fun getUserSubscriptions(): Flow<List<Subscription>> {
        val userId = auth.currentUser?.uid ?: return flowOf(emptyList())
        
        return userSubscriptionsCollection
            .whereEqualTo("userId", userId)
            .snapshotFlow()
            .map { snapshot: QuerySnapshot ->
                snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Subscription::class.java)?.copy(id = doc.id)
                }
            }
            .catch { emit(emptyList()) }
    }

    // Kullanıcı için yeni üyelik ekle
    suspend fun addSubscription(subscription: Subscription): Result<String> = try {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
        
        val docRef = userSubscriptionsCollection.document()
        val subscriptionWithIds = subscription.copy(
            id = docRef.id,
            userId = userId
        )
        
        docRef.set(subscriptionWithIds).await()
        Result.success(docRef.id)
    } catch (e: Exception) {
        Result.failure(e)
    }

    // Üyelik güncelle
    suspend fun updateSubscription(subscription: Subscription) {
        try {
            val userId = auth.currentUser?.uid ?: return
            
            firestore.collection("users")
                .document(userId)
                .collection("subscriptions")
                .document(subscription.id)
                .set(subscription)
                .await()
        } catch (e: Exception) {
            // Hata durumunda sessizce devam et
        }
    }

    // Üyelik sil
    suspend fun deleteSubscription(subscriptionId: String) {
        try {
            val userId = auth.currentUser?.uid ?: return
            
            firestore.collection("users")
                .document(userId)
                .collection("subscriptions")
                .document(subscriptionId)
                .delete()
                .await()
        } catch (e: Exception) {
            // Hata durumunda sessizce devam et
        }
    }
} 