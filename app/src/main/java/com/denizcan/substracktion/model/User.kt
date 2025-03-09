package com.denizcan.substracktion.model

import java.util.Currency

data class User(
    val id: String = "",
    val email: String = "",
    val displayName: String = "",
    val country: String = "TR",
    val currencyCode: String = "TRY",
    val notificationsEnabled: Boolean = true,
    val emailNotifications: Boolean = true,
    val darkMode: Boolean = false  // Dark mode tercihi eklendi
)

data class UserRegion(
    val country: String,              // Ülke kodu (TR, US, vb.)
    val currency: Currency,           // Tercih edilen para birimi
    val timezone: String,             // Saat dilimi
    val isAutoDetected: Boolean = false // Otomatik tespit edildi mi?
)

data class UserPreferences(
    val language: String = "tr",      // Tercih edilen dil
    val notificationsEnabled: Boolean = true,  // Bildirimler açık mı?
    val subscriptionRenewalAlert: Boolean = true,  // Yenileme uyarıları
    val priceChangeAlert: Boolean = true,         // Fiyat değişikliği uyarıları
    val darkMode: Boolean = false,               // Karanlık mod
    val emailNotifications: Boolean = true       // Email bildirimleri
) 