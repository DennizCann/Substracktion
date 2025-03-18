package com.denizcan.substracktion.util

import java.util.Currency
import java.util.Locale

data class CountryInfo(
    val code: String,        // TR, US, GB gibi
    val name: String,        // Turkey, United States, United Kingdom gibi
    val currencyCode: String,// TRY, USD, GBP gibi
    val currencyName: String,// Turkish Lira, US Dollar, British Pound gibi
    val currencySymbol: String // ₺, $, £ gibi
)

object CountryCurrencyManager {
    private val supportedCountries = listOf(
        "TR", "US", "GB", "EU", "JP", "CN", "RU", "AE", "SA", "CH", 
        "AU", "CA", "NZ", "SG", "HK", "KR", "IN", "BR", "ZA"
    )

    fun getCountryList(language: Language): List<CountryInfo> {
        return supportedCountries.mapNotNull { countryCode ->
            try {
                val locale = Locale("", countryCode)
                val currency = Currency.getInstance(locale)
                
                CountryInfo(
                    code = countryCode,
                    name = when (language) {
                        Language.TURKISH -> locale.getDisplayCountry(Locale("tr"))
                        Language.ENGLISH -> locale.getDisplayCountry(Locale.ENGLISH)
                    },
                    currencyCode = currency.currencyCode,
                    currencyName = when (language) {
                        Language.TURKISH -> getTurkishCurrencyName(currency.currencyCode)
                        Language.ENGLISH -> currency.displayName
                    },
                    currencySymbol = currency.symbol
                )
            } catch (e: Exception) {
                null
            }
        }.sortedBy { it.name }
    }

    private fun getTurkishCurrencyName(code: String): String {
        return when (code) {
            "TRY" -> "Türk Lirası"
            "USD" -> "Amerikan Doları"
            "EUR" -> "Euro"
            "GBP" -> "İngiliz Sterlini"
            "JPY" -> "Japon Yeni"
            "CNY" -> "Çin Yuanı"
            "RUB" -> "Rus Rublesi"
            "AED" -> "BAE Dirhemi"
            "SAR" -> "Suudi Riyali"
            "CHF" -> "İsviçre Frangı"
            "AUD" -> "Avustralya Doları"
            "CAD" -> "Kanada Doları"
            "NZD" -> "Yeni Zelanda Doları"
            "SGD" -> "Singapur Doları"
            "HKD" -> "Hong Kong Doları"
            "KRW" -> "Güney Kore Wonu"
            "INR" -> "Hindistan Rupisi"
            "BRL" -> "Brezilya Reali"
            "ZAR" -> "Güney Afrika Randı"
            else -> code
        }
    }

    fun getCurrencyForCountry(countryCode: String): String {
        return when (countryCode) {
            "TR" -> "TRY"
            "US" -> "USD"
            "GB" -> "GBP"
            "EU" -> "EUR"
            else -> "TRY"
        }
    }
} 