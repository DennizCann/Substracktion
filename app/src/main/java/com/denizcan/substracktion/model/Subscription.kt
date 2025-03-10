package com.denizcan.substracktion.model

import java.util.Date
import java.util.UUID
import java.util.Currency

data class Subscription(
    val id: String = UUID.randomUUID().toString(),
    val name: String,                    // Üyelik adı (Netflix, Spotify vb.)
    val category: SubscriptionCategory,  // Üyelik kategorisi
    val plan: SubscriptionPlan,         // Seçilen plan bilgileri
)

data class SubscriptionPlan(
    val name: String,        // Plan adı (Basic, Premium vb.)
    val price: Double,       // Plan fiyatı
    val currency: Currency,    // Para birimi (TRY, USD vb.)
    val billingPeriod: BillingPeriod  // Faturalandırma periyodu
)

enum class SubscriptionCategory {
    STREAMING,      // Netflix, Disney+, Spotify vb.
    GAMING,         // PlayStation Plus, Xbox Game Pass vb.
    PRODUCTIVITY,   // Microsoft 365, Adobe CC vb.
    CLOUD_STORAGE,  // Google One, iCloud, Dropbox vb.
    FITNESS,        // Strava Premium, Nike Training Club vb.
    NEWS,           // NY Times, Washington Post vb.
    EDUCATION,      // Udemy, Coursera vb.
    OTHER           // Diğer kategoriler
}

enum class BillingPeriod {
    MONTHLY,
    QUARTERLY,
    SEMI_ANNUALLY,
    ANNUALLY
}

// Kullanıcının üyelikleri için model
data class UserSubscription(
    val id: String = UUID.randomUUID().toString(),
    val templateId: String?,            // Hazır üyelik şablonundan eklendiyse referansı
    val name: String,                   
    val category: SubscriptionCategory,  
    val subscriptionHistory: List<SubscriptionPeriod>, // Üyelik geçmişi
    val userId: String,                 
    val isActive: Boolean = true,        // Üyelik aktif mi?
    val plan: SubscriptionPlan,         // Seçilen plan bilgileri
)

// Üyelik dönemlerini takip etmek için
data class SubscriptionPeriod(
    val startDate: Date,
    val endDate: Date? = null,          // null ise halen devam ediyor
    val plan: SubscriptionPlan
)

// Bölgesel plan bilgileri için yeni model
data class RegionalPlan(
    val region: String,        // Örn: "TR", "US", "EU" vb.
    val plans: List<SubscriptionPlan>
)

// SubscriptionTemplate sınıfını güncelliyoruz
data class SubscriptionTemplate(
    val id: String,
    val name: String,
    val category: SubscriptionCategory,
    val regionalPlans: Map<String, List<SubscriptionPlan>>, // Bölgeye göre planlar
    val availableRegions: List<String>,    // Hizmetin sunulduğu bölgeler
    val logoUrl: String? = null,
    val description: String? = null,
    val defaultRegion: String = "TR"       // Varsayılan bölge
)

// Para birimi için yeni data class
data class Currency(
    val code: String,      // Örn: "TRY", "USD", "EUR"
    val symbol: String,    // Örn: "₺", "$", "€"
    val name: String       // Örn: "Turkish Lira", "US Dollar", "Euro"
) 