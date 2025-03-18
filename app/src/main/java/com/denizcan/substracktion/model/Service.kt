package com.denizcan.substracktion.model

data class Service(
    val id: String = "",
    val name: String = "",
    val logoUrl: String = "",
    val category: String = ""
)

data class ServicePlan(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val prices: Map<String, Price> = emptyMap()  // Ülke kodu -> Fiyat bilgisi
)

data class Price(
    val amount: Double = 0.0,
    val currency: String = ""
) 