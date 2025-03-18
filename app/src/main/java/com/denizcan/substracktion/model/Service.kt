package com.denizcan.substracktion.model

data class Service(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val logoUrl: String = "",
    val website: String = "",
    val supportedCountries: List<String> = emptyList()
)

data class ServicePlan(
    val id: String = "",
    val serviceId: String = "",
    val name: String = "",
    val description: String = "",
    val prices: Map<String, Price> = emptyMap() // Ülke -> Fiyat şeklinde basit map
)

data class Price(
    val amount: Double = 0.0,
    val currency: String = ""
) 