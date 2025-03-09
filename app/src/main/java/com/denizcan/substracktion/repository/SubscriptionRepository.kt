package com.denizcan.substracktion.repository

import com.denizcan.substracktion.model.SubscriptionPeriod
import com.denizcan.substracktion.model.SubscriptionPlan
import com.denizcan.substracktion.model.SubscriptionTemplate
import com.denizcan.substracktion.model.UserSubscription
import com.denizcan.substracktion.util.snapshotFlow
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class SubscriptionRepository(
    private val firestore: FirebaseFirestore
) {
    // Hazır üyelik şablonlarını getir
    suspend fun getSubscriptionTemplates(): Flow<List<SubscriptionTemplate>> {
        return firestore.collection("subscriptionTemplates")
            .snapshotFlow()
            .map { snapshot -> snapshot.toObjects(SubscriptionTemplate::class.java) }
    }

    // Kullanıcının üyeliklerini getir
    suspend fun getUserSubscriptions(userId: String): Flow<List<UserSubscription>> {
        return firestore.collection("userSubscriptions")
            .document(userId)
            .collection("subscriptions")
            .snapshotFlow()
            .map { snapshot -> snapshot.toObjects(UserSubscription::class.java) }
    }

    // Tek bir üyeliği getir
    private suspend fun getUserSubscription(userId: String, subscriptionId: String): UserSubscription {
        return firestore.collection("userSubscriptions")
            .document(userId)
            .collection("subscriptions")
            .document(subscriptionId)
            .get()
            .await()
            .toObject(UserSubscription::class.java)
            ?: throw IllegalStateException("Subscription not found")
    }

    // Yeni üyelik ekle
    suspend fun addSubscription(subscription: UserSubscription) {
        firestore.collection("userSubscriptions")
            .document(subscription.userId)
            .collection("subscriptions")
            .document(subscription.id)
            .set(subscription)
            .await()
    }

    // Üyelik planını güncelle
    suspend fun updateSubscriptionPlan(
        userId: String,
        subscriptionId: String,
        newPeriod: SubscriptionPeriod
    ) {
        // Mevcut üyeliği al
        val subscription = getUserSubscription(userId, subscriptionId)
        
        // Yeni dönemi ekle
        val updatedHistory = subscription.subscriptionHistory + newPeriod
        
        // Güncelle
        firestore.collection("userSubscriptions")
            .document(userId)
            .collection("subscriptions")
            .document(subscriptionId)
            .update("subscriptionHistory", updatedHistory)
            .await()
    }

    // Belirli bir bölge için üyelik şablonlarını getir
    suspend fun getSubscriptionTemplatesForRegion(region: String): Flow<List<SubscriptionTemplate>> {
        return firestore.collection("subscriptionTemplates")
            .whereArrayContains("availableRegions", region)
            .snapshotFlow()
            .map { snapshot -> snapshot.toObjects(SubscriptionTemplate::class.java) }
    }

    // Bir üyeliğin belirli bir bölgedeki planlarını getir
    suspend fun getPlansForRegion(templateId: String, region: String): Flow<List<SubscriptionPlan>> {
        return firestore.collection("subscriptionTemplates")
            .document(templateId)
            .snapshotFlow()
            .map { snapshot -> 
                snapshot.toObject(SubscriptionTemplate::class.java)
                    ?.regionalPlans
                    ?.get(region) 
                    ?: emptyList()
            }
    }
} 