package com.denizcan.substracktion.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.denizcan.substracktion.util.Language
import com.denizcan.substracktion.util.UiText
import com.denizcan.substracktion.viewmodel.ProfileViewModel
import androidx.compose.foundation.shape.CircleShape
import com.denizcan.substracktion.components.CurrencySelectionDialog
import com.denizcan.substracktion.components.SelectionDialogs
import com.denizcan.substracktion.components.EditNameDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBackToHome: () -> Unit,
    viewModel: ProfileViewModel,
    language: Language,
    onDeleteAccountClick: () -> Unit
) {
    val user by viewModel.user.collectAsState()
    val drawerText = remember(language) { UiText.getDrawerText(language) }
    val commonText = remember(language) { UiText.getCommonText(language) }

    var showRegionDialog by remember { mutableStateOf(false) }
    var showCurrencyDialog by remember { mutableStateOf(false) }
    var showEditNameDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(drawerText.profile) },
                navigationIcon = {
                    IconButton(onClick = onBackToHome) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = commonText.back
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Kullanıcı Profil Başlığı
            UserProfileHeader(
                displayName = user?.displayName ?: "",
                email = user?.email ?: "",
                onEditNameClick = { showEditNameDialog = true },
                language = language
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Bölge ve Para Birimi Seçimi
            ListItem(
                headlineContent = { Text(drawerText.country) },
                supportingContent = { Text(user?.country ?: "TR") },
                leadingContent = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                modifier = Modifier.clickable { showRegionDialog = true }
            )

            ListItem(
                headlineContent = { Text(drawerText.currency) },
                supportingContent = { Text(user?.currencyCode ?: "TRY") },
                leadingContent = { Icon(Icons.Default.Payments, contentDescription = null) },
                modifier = Modifier.clickable { showCurrencyDialog = true }
            )

            // Hesap Silme
            ListItem(
                headlineContent = { 
                    Text(
                        drawerText.deleteAccount,
                        color = MaterialTheme.colorScheme.error
                    )
                },
                leadingContent = { 
                    Icon(
                        Icons.Default.Delete,
                        tint = MaterialTheme.colorScheme.error,
                        contentDescription = null
                    )
                },
                modifier = Modifier.clickable(onClick = onDeleteAccountClick)
            )
        }
    }

    // Dialog'lar
    if (showRegionDialog) {
        SelectionDialogs(
            onDismiss = { showRegionDialog = false },
            onRegionSelected = { region ->
                viewModel.updateCountry(region)
                showRegionDialog = false
            },
            language = language
        )
    }

    if (showCurrencyDialog) {
        CurrencySelectionDialog(
            onDismiss = { showCurrencyDialog = false },
            onCurrencySelected = { currency ->
                viewModel.updateCurrency(currency.currencyCode)
                showCurrencyDialog = false
            },
            language = language
        )
    }

    if (showEditNameDialog) {
        EditNameDialog(
            currentName = user?.displayName ?: "",
            onDismiss = { showEditNameDialog = false },
            onNameChange = { newName ->
                viewModel.updateDisplayName(newName)
            }
        )
    }
}

@Composable
private fun UserProfileHeader(
    displayName: String,
    email: String,
    onEditNameClick: () -> Unit,
    language: Language
) {
    val commonText = remember(language) { UiText.getCommonText(language) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Surface(
            modifier = Modifier.size(100.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        ) {
            Icon(
                Icons.Default.Person,
                null,
                modifier = Modifier
                    .padding(16.dp)
                    .size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(Modifier.height(16.dp))
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = onEditNameClick)
        ) {
            Text(
                displayName,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = commonText.edit,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        Text(
            email,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
} 