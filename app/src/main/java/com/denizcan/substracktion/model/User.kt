package com.denizcan.substracktion.model


data class User(
    val id: String = "",
    val email: String = "",
    val displayName: String = "",
    val country: String = "TR",
    val currencyCode: String = "TRY",
    val notificationsEnabled: Boolean = true,  // Sadece uygulama bildirimleri kalsın
    val photoUrl: String? = null
)
