package com.denizcan.substracktion.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.denizcan.substracktion.util.Language
import com.denizcan.substracktion.util.CountryCurrencyManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryPickerDialog(
    onDismiss: () -> Unit,
    onCountrySelected: (String) -> Unit,
    language: Language
) {
    var searchQuery by remember { mutableStateOf("") }
    val countries = CountryCurrencyManager.getCountryList(language)
    val filteredCountries = remember(searchQuery, countries) {
        if (searchQuery.isEmpty()) {
            countries
        } else {
            countries.filter { country ->
                country.name.contains(searchQuery, ignoreCase = true) ||
                country.code.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (language == Language.TURKISH) "Ülke Seçin" else "Select Country")
        },
        text = {
            Column {
                // Arama çubuğu
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    placeholder = {
                        Text(if (language == Language.TURKISH) "Ülke ara..." else "Search country...")
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    singleLine = true
                )

                // Ülke listesi
                LazyColumn(
                    modifier = Modifier.heightIn(max = 400.dp)
                ) {
                    items(filteredCountries) { country ->
                        ListItem(
                            headlineContent = { Text(country.name) },
                            supportingContent = { Text(country.code) },
                            modifier = Modifier.clickable {
                                onCountrySelected(country.code)
                                onDismiss()
                            }
                        )
                        Divider()
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(if (language == Language.TURKISH) "İptal" else "Cancel")
            }
        }
    )
} 