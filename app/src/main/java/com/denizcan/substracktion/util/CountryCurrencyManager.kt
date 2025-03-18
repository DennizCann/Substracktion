package com.denizcan.substracktion.util

import java.util.Currency
import java.util.Locale

data class CountryInfo(
    val code: String,        // TR, US gibi
    val name: String,        // Turkey, United States gibi
    val currencyCode: String,// TRY, USD gibi
    val currencyName: String,// Turkish Lira, US Dollar gibi
    val currencySymbol: String // ₺, $ gibi
)

object CountryCurrencyManager {
    fun getCountryList(language: Language): List<CountryInfo> {
        return when (language) {
            Language.TURKISH -> listOf(
                CountryInfo("TR", "Türkiye", "TRY", "Türk Lirası", "₺"),
                CountryInfo("US", "Amerika Birleşik Devletleri", "USD", "Amerikan Doları", "$")
            )
            Language.ENGLISH -> listOf(
                CountryInfo("TR", "Turkey", "TRY", "Turkish Lira", "₺"),
                CountryInfo("US", "United States", "USD", "US Dollar", "$")
            )
        }
    }

    fun getCurrencyForCountry(countryCode: String): String {
        return when (countryCode.uppercase()) {
            "TR" -> "TRY"
            "US" -> "USD"
            else -> "USD" // Varsayılan
        }
    }
} 