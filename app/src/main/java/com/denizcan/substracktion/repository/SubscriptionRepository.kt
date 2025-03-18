package com.denizcan.substracktion.repository

import com.denizcan.substracktion.model.*
import com.denizcan.substracktion.util.snapshotFlow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

    // Tüm servisleri getir
    fun getServices(): Flow<List<Service>> {
        return servicesCollection
            .snapshotFlow()
            .map { snapshot -> 
                snapshot.documents.mapNotNull { 
                    it.toObject(Service::class.java) 
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
            .map { snapshot ->
                snapshot.documents.mapNotNull {
                    it.toObject(ServicePlan::class.java)
                }
            }
            .catch { emit(emptyList()) }
    }

    // Kullanıcının üyeliklerini getir
    fun getUserSubscriptions(): Flow<List<Subscription>> {
        val userId = auth.currentUser?.uid ?: return flowOf(emptyList())
        
        return firestore.collection("users")
            .document(userId)
            .collection("subscriptions")
            .snapshotFlow()
            .map { snapshot ->
                snapshot.documents.mapNotNull {
                    it.toObject(Subscription::class.java)
                }
            }
            .catch { emit(emptyList()) }
    }

    // Yeni üyelik ekle
    suspend fun addSubscription(subscription: Subscription) {
        try {
            val userId = auth.currentUser?.uid ?: return
            
            firestore.collection("users")
                .document(userId)
                .collection("subscriptions")
                .add(subscription.copy(userId = userId))
                .await()
        } catch (e: Exception) {
            // Hata durumunda sessizce devam et
        }
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