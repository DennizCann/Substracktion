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
import androidx.compose.ui.text.input.PasswordVisualTransformation

// Tüm dialog state'lerini tek bir sealed class'ta toplayabiliriz
sealed class ProfileDialogState {
    object None : ProfileDialogState()
    object Region : ProfileDialogState()
    object EditName : ProfileDialogState()
    object DeleteAccount : ProfileDialogState()
    object DeleteAccountConfirm : ProfileDialogState()
    object Language : ProfileDialogState()
    object DeleteAccountEmailSent : ProfileDialogState()
}

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
    val isPasswordProvider by viewModel.isPasswordProvider.collectAsState()

    var dialogState by remember { mutableStateOf<ProfileDialogState>(ProfileDialogState.None) }

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
                onEditNameClick = { dialogState = ProfileDialogState.EditName },
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
                        checked = user?.notificationsEnabled == true,
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
                modifier = Modifier.clickable { dialogState = ProfileDialogState.Region }
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
                modifier = Modifier.clickable { dialogState = ProfileDialogState.Language }
            )

            Divider()

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
                modifier = Modifier.clickable { dialogState = ProfileDialogState.DeleteAccount }
            )
        }
    }

    // SelectionDialogs'u sadece ülke seçimi için kullanacağız
    if (dialogState is ProfileDialogState.Region) {
        AlertDialog(
            onDismissRequest = { dialogState = ProfileDialogState.None },
            title = { Text(if (language == Language.TURKISH) "Ülke Seç" else "Select Country") },
            text = {
                LazyColumn {
                    items(
                        items = CountryCurrencyManager.getCountryList(language),
                        key = { country -> country.code }
                    ) { country ->
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
                                        dialogState = ProfileDialogState.None
                                    }
                                )
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { dialogState = ProfileDialogState.None }) {
                    Text(if (language == Language.TURKISH) "Tamam" else "OK")
                }
            }
        )
    }

    // İsim değiştirme dialog'u
    if (dialogState is ProfileDialogState.EditName) {
        EditNameDialog(
            currentName = user?.displayName ?: "",
            onDismiss = { dialogState = ProfileDialogState.None },
            onNameChange = { newName ->
                viewModel.updateDisplayName(newName)
                dialogState = ProfileDialogState.None
            },
            language = language
        )
    }

    // Hesap silme dialog'u
    if (dialogState is ProfileDialogState.DeleteAccount) {
        AlertDialog(
            onDismissRequest = { dialogState = ProfileDialogState.None },
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
                        if (isPasswordProvider) {
                            dialogState = ProfileDialogState.DeleteAccountConfirm
                        } else {
                            // Google hesabı için e-posta doğrulaması gönder
                            viewModel.sendGoogleAccountDeletionEmail(
                                onEmailSent = {
                                    dialogState = ProfileDialogState.DeleteAccountEmailSent
                                },
                                onError = {
                                    // Hata durumunu göster
                                }
                            )
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(
                        if (language == Language.TURKISH) 
                            "Devam Et"
                        else 
                            "Continue"
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { dialogState = ProfileDialogState.None }) {
                    Text(commonText.cancel)
                }
            }
        )
    }

    // Şifre doğrulama dialog'u
    if (dialogState is ProfileDialogState.DeleteAccountConfirm) {
        var password by remember { mutableStateOf("") }
        var showError by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { dialogState = ProfileDialogState.None },
            title = {
                Text(
                    if (language == Language.TURKISH)
                        "Şifrenizi Doğrulayın"
                    else
                        "Confirm Your Password"
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = password,
                        onValueChange = { 
                            password = it
                            showError = false 
                        },
                        label = {
                            Text(
                                if (language == Language.TURKISH)
                                    "Şifre"
                                else
                                    "Password"
                            )
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        isError = showError,
                        supportingText = if (showError) {
                            {
                                Text(
                                    if (language == Language.TURKISH)
                                        "Hatalı şifre"
                                    else
                                        "Incorrect password",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        } else null
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteAccountWithPassword(
                            password = password,
                            onSuccess = {
                                dialogState = ProfileDialogState.None
                                onDeleteAccountClick()
                            },
                            onError = {
                                showError = true
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
                TextButton(onClick = { dialogState = ProfileDialogState.None }) {
                    Text(commonText.cancel)
                }
            }
        )
    }

    // Dil seçim dialog'u
    if (dialogState is ProfileDialogState.Language) {
        AlertDialog(
            onDismissRequest = { dialogState = ProfileDialogState.None },
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
                                    dialogState = ProfileDialogState.None
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
                                    dialogState = ProfileDialogState.None
                                }
                            )
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { dialogState = ProfileDialogState.None }) {
                    Text(if (language == Language.TURKISH) "Tamam" else "OK")
                }
            }
        )
    }

    // E-posta gönderildi dialog'u
    if (dialogState is ProfileDialogState.DeleteAccountEmailSent) {
        AlertDialog(
            onDismissRequest = { dialogState = ProfileDialogState.None },
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
                        "Hesabınızı silmek için e-posta adresinize gönderilen bağlantıya tıklayın."
                    else
                        "Please check your email and click the link to delete your account."
                )
            },
            confirmButton = {
                TextButton(onClick = { dialogState = ProfileDialogState.None }) {
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
