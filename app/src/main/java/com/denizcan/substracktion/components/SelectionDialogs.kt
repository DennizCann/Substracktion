package com.denizcan.substracktion.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import java.util.*
import com.denizcan.substracktion.util.UiText
import com.denizcan.substracktion.util.Language

@Composable
fun SelectionDialogs(
    onDismiss: () -> Unit,
    onRegionSelected: (String) -> Unit,
    language: Language
) {
    val text = remember(language) { UiText.getProfileText(language) }
    val regions = remember(language) {
        when (language) {
            Language.TURKISH -> listOf(
                "TR" to "Türkiye",
                "US" to "Amerika Birleşik Devletleri",
                "GB" to "Birleşik Krallık",
                "DE" to "Almanya",
                "FR" to "Fransa",
                "IT" to "İtalya",
                "ES" to "İspanya"
            )
            Language.ENGLISH -> listOf(
                "TR" to "Turkey",
                "US" to "United States",
                "GB" to "United Kingdom",
                "DE" to "Germany",
                "FR" to "France",
                "IT" to "Italy",
                "ES" to "Spain"
            )
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text.region) },
        text = {
            LazyColumn {
                items(regions) { (code, name) ->
                    ListItem(
                        headlineContent = { Text(name) },
                        modifier = Modifier.clickable { onRegionSelected(code) }
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (language == Language.ENGLISH) "Cancel" else "İptal")
            }
        }
    )
}

@Composable
fun CurrencySelectionDialog(
    onDismiss: () -> Unit,
    onCurrencySelected: (Currency) -> Unit,
    language: Language
) {
    val text = remember(language) { UiText.getProfileText(language) }
    val currencies = remember(language) {
        val currencyList = listOf(
            "TRY" to (if (language == Language.TURKISH) "Türk Lirası" else "Turkish Lira"),
            "USD" to (if (language == Language.TURKISH) "Amerikan Doları" else "US Dollar"),
            "EUR" to (if (language == Language.TURKISH) "Euro" else "Euro"),
            "GBP" to (if (language == Language.TURKISH) "İngiliz Sterlini" else "British Pound"),
            "JPY" to (if (language == Language.TURKISH) "Japon Yeni" else "Japanese Yen"),
            "CHF" to (if (language == Language.TURKISH) "İsviçre Frangı" else "Swiss Franc"),
            "AUD" to (if (language == Language.TURKISH) "Avustralya Doları" else "Australian Dollar"),
            "CAD" to (if (language == Language.TURKISH) "Kanada Doları" else "Canadian Dollar")
        )
        
        currencyList.map { (code, name) ->
            CurrencyInfo(
                currency = Currency.getInstance(code),
                localizedName = name
            )
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text.currency) },
        text = {
            LazyColumn {
                items(currencies) { currencyInfo ->
                    ListItem(
                        headlineContent = { Text(currencyInfo.localizedName) },
                        modifier = Modifier.clickable { onCurrencySelected(currencyInfo.currency) }
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (language == Language.ENGLISH) "Cancel" else "İptal")
            }
        }
    )
}

private data class CurrencyInfo(
    val currency: Currency,
    val localizedName: String
) 