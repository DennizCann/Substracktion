package com.denizcan.substracktion.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.denizcan.substracktion.components.EditNameDialog
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import com.denizcan.substracktion.util.CountryCurrencyManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBackToHome: () -> Unit,
    viewModel: ProfileViewModel,
    language: Language,
    onLanguageChange: (Language) -> Unit,
    onDeleteAccountClick: () -> Unit
) {
    val user by viewModel.user.collectAsState()
    val drawerText = remember(language) { UiText.getDrawerText(language) }
    val commonText = remember(language) { UiText.getCommonText(language) }
    val isEmailVerified by viewModel.isEmailVerified.collectAsState()
    val verificationEmailSent by viewModel.verificationEmailSent.collectAsState()
    val isPasswordProvider by viewModel.isPasswordProvider.collectAsState()

    var showRegionDialog by remember { mutableStateOf(false) }
    var showEditNameDialog by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { viewModel.uploadProfilePhoto(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (language == Language.TURKISH)
                            "Profil ve Ayarlar"
                        else
                            "Profile & Settings"
                    )
                },
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
                photoUrl = user?.photoUrl,
                onEditNameClick = { showEditNameDialog = true },
                onPhotoClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                language = language
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Bildirim Ayarları
            ListItem(
                headlineContent = {
                    Text(
                        if (language == Language.TURKISH)
                            "Bildirimler"
                        else
                            "Notifications"
                    )
                },
                supportingContent = {
                    Text(
                        if (language == Language.TURKISH)
                            "Uygulama bildirimlerini al"
                        else
                            "Receive app notifications"
                    )
                },
                leadingContent = {
                    Icon(Icons.Default.Notifications, contentDescription = null)
                },
                trailingContent = {
                    Switch(
                        checked = user?.notificationsEnabled ?: false,
                        onCheckedChange = { viewModel.updateNotifications(it) }
                    )
                }
            )

            Divider()

            // Bölge ve Para Birimi
            ListItem(
                headlineContent = { Text(drawerText.country) },
                supportingContent = { Text(user?.country ?: "TR") },
                leadingContent = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                modifier = Modifier.clickable { showRegionDialog = true }
            )

            // Para birimi artık tıklanamaz, sadece bilgi amaçlı
            ListItem(
                headlineContent = { Text(drawerText.currency) },
                supportingContent = {
                    val countryInfo = CountryCurrencyManager.getCountryList(language)
                        .find { it.code == (user?.country ?: "TR") }
                    Text(
                        "${countryInfo?.currencyName ?: "TRY"} (${countryInfo?.currencySymbol ?: "₺"})"
                    )
                },
                leadingContent = { Icon(Icons.Default.Payments, contentDescription = null) }
            )

            Divider()

            // Dil seçimi
            ListItem(
                headlineContent = { Text(drawerText.language) },
                supportingContent = {
                    Text(if (language == Language.TURKISH) "Türkçe" else "English")
                },
                leadingContent = {
                    Icon(Icons.Default.Language, contentDescription = null)
                },
                modifier = Modifier.clickable { showLanguageDialog = true }
            )

            Divider()

            // E-posta doğrulama durumu - sadece e-posta/şifre ile giriş yapanlar için göster
            if (!isEmailVerified && isPasswordProvider) {
                ListItem(
                    headlineContent = { 
                        Text(
                            if (language == Language.TURKISH)
                                "E-posta Doğrulama"
                            else
                                "Email Verification"
                        )
                    },
                    supportingContent = {
                        Text(
                            if (language == Language.TURKISH)
                                "E-posta adresiniz doğrulanmamış"
                            else
                                "Your email is not verified"
                        )
                    },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    },
                    trailingContent = {
                        TextButton(
                            onClick = { viewModel.sendVerificationEmail() },
                            enabled = !verificationEmailSent
                        ) {
                            Text(
                                if (language == Language.TURKISH)
                                    if (verificationEmailSent) "Gönderildi" else "Doğrula"
                                else
                                    if (verificationEmailSent) "Sent" else "Verify"
                            )
                        }
                    }
                )

                Divider()
            }

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
                modifier = Modifier.clickable { showDeleteAccountDialog = true }
            )
        }
    }

    // SelectionDialogs'u sadece ülke seçimi için kullanacağız
    if (showRegionDialog) {
        AlertDialog(
            onDismissRequest = { showRegionDialog = false },
            title = { Text(if (language == Language.TURKISH) "Ülke Seç" else "Select Country") },
            text = {
                LazyColumn {
                    items(CountryCurrencyManager.getCountryList(language)) { country ->
                        ListItem(
                            headlineContent = { Text(country.name) },
                            supportingContent = {
                                Text(
                                    "${country.currencyName} (${country.currencySymbol})"
                                )
                            },
                            leadingContent = {
                                RadioButton(
                                    selected = country.code == (user?.country ?: "TR"),
                                    onClick = {
                                        viewModel.updateCountry(country.code)
                                        showRegionDialog = false
                                    }
                                )
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showRegionDialog = false }) {
                    Text(if (language == Language.TURKISH) "Tamam" else "OK")
                }
            }
        )
    }

    // İsim değiştirme dialog'u
    if (showEditNameDialog) {
        EditNameDialog(
            currentName = user?.displayName ?: "",
            onDismiss = { showEditNameDialog = false },
            onNameChange = { newName ->
                viewModel.updateDisplayName(newName)
                showEditNameDialog = false
            },
            language = language
        )
    }

    // Hesap silme dialog'u
    if (showDeleteAccountDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAccountDialog = false },
            title = {
                Text(
                    if (language == Language.TURKISH)
                        "Hesabınızı Silmek İstiyor musunuz?"
                    else
                        "Delete Your Account?"
                )
            },
            text = {
                Text(
                    if (language == Language.TURKISH)
                        "Bu işlem geri alınamaz. Tüm verileriniz silinecektir."
                    else
                        "This action cannot be undone. All your data will be permanently deleted."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteAccount(
                            onSuccess = {
                                showDeleteAccountDialog = false
                                onDeleteAccountClick()
                            }
                        )
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(
                        if (language == Language.TURKISH) "Hesabı Sil" else "Delete Account"
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAccountDialog = false }) {
                    Text(commonText.cancel)
                }
            }
        )
    }

    // Dil seçim dialog'u
    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text("Select Language / Dil Seçin") },
            text = {
                Column {
                    ListItem(
                        headlineContent = { Text("Türkçe") },
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
                        headlineContent = { Text("English") },
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
                    Text(if (language == Language.TURKISH) "Tamam" else "OK")
                }
            }
        )
    }

    // Doğrulama e-postası gönderildi dialog'u
    if (verificationEmailSent) {
        AlertDialog(
            onDismissRequest = { /* Dialog'u kapat */ },
            title = {
                Text(
                    if (language == Language.TURKISH)
                        "Doğrulama E-postası Gönderildi"
                    else
                        "Verification Email Sent"
                )
            },
            text = {
                Text(
                    if (language == Language.TURKISH)
                        "Lütfen e-posta adresinizi kontrol edin ve doğrulama bağlantısına tıklayın."
                    else
                        "Please check your email and click the verification link."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.refreshEmailVerificationStatus()
                        /* Dialog'u kapat */
                    }
                ) {
                    Text(if (language == Language.TURKISH) "Tamam" else "OK")
                }
            }
        )
    }
}

@Composable
private fun UserProfileHeader(
    displayName: String,
    email: String,
    photoUrl: String?,
    onEditNameClick: () -> Unit,
    onPhotoClick: () -> Unit,
    language: Language
) {
    val commonText = remember(language) { UiText.getCommonText(language) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        // Profil fotoğrafı
        Surface(
            modifier = Modifier
                .size(100.dp)
                .clickable(onClick = onPhotoClick),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        ) {
            if (photoUrl != null) {
                AsyncImage(
                    model = photoUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    Icons.Default.Person,
                    null,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
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