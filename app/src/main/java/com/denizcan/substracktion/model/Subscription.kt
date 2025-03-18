package com.denizcan.substracktion.model

import java.util.Date

data class Subscription(
    val id: String = "",
    val userId: String = "",
    val serviceId: String? = null,    // Hazır servis için
    val planId: String? = null,       // Hazır plan için
    val customName: String? = null,   // Özel üyelik için
    val amount: Double = 0.0,
    val currency: String = "",
    val billingPeriod: BillingPeriod = BillingPeriod.MONTHLY,
    val nextBillingDate: Date = Date(),
    val isActive: Boolean = true
)

enum class BillingPeriod {
    MONTHLY, YEARLY, QUARTERLY, WEEKLY
} 