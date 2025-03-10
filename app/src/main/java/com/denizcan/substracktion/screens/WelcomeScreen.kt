package com.denizcan.substracktion.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.denizcan.substracktion.util.Language
import com.denizcan.substracktion.util.UiText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    onNavigateToNext: () -> Unit,
    language: Language,
    onLanguageChange: (Language) -> Unit
) {
    var showLanguageDialog by remember { mutableStateOf(false) }
    val welcomeText = remember(language) { UiText.getWelcomeText(language) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            TextButton(
                onClick = { showLanguageDialog = true },
                modifier = Modifier.align(Alignment.End),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    Icons.Default.Language,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    if (language == Language.TURKISH) "Türkçe" else "English",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = welcomeText.slogan,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(welcomeText.features) { feature ->
                FeatureItem(feature)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 32.dp)
        ) {
            Button(
                onClick = onNavigateToNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                Text(
                    welcomeText.getStarted,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }

    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { 
                Text(
                    "Select Language / Dil Seçin",
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    ListItem(
                        headlineContent = { 
                            Text(
                                "Türkçe",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        leadingContent = {
                            RadioButton(
                                selected = language == Language.TURKISH,
                                onClick = { 
                                    onLanguageChange(Language.TURKISH)
                                    showLanguageDialog = false
                                }
                            )
                        }
                    )
                    ListItem(
                        headlineContent = { 
                            Text(
                                "English",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        leadingContent = {
                            RadioButton(
                                selected = language == Language.ENGLISH,
                                onClick = { 
                                    onLanguageChange(Language.ENGLISH)
                                    showLanguageDialog = false
                                }
                            )
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text(
                        if (language == Language.TURKISH) "Tamam" else "OK",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        )
    }
}

@Composable
private fun FeatureItem(feature: UiText.WelcomeText.Feature) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = feature.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = feature.description,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 20.sp
            )
        }
    }
} 