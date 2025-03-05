package com.denizcan.substracktion.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.denizcan.substracktion.R
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.ui.text.withStyle
import com.denizcan.substracktion.util.Language
import com.denizcan.substracktion.util.UiText
import com.denizcan.substracktion.viewmodel.AppViewModel

@Composable
fun AuthScreen(
    onEmailSignIn: () -> Unit,
    onGoogleSignIn: () -> Unit,
    onRegister: () -> Unit,
    language: Language,
    onLanguageChange: (Language) -> Unit,
    viewModel: AppViewModel,
    onShowPrivacyPolicy: () -> Unit
) {
    var showContent by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    val text = remember(language) { UiText.getAuthText(language) }
    
    // İçeriği göstermek için LaunchedEffect ekleyelim
    LaunchedEffect(Unit) {
        showContent = true
    }

    // Dil seçme dialog'u
    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { 
                Text(
                    text = text.selectLanguage,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Language.values().forEach { lang ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { 
                                    onLanguageChange(lang)
                                    showLanguageDialog = false
                                },
                            shape = MaterialTheme.shapes.medium,
                            color = if (language == lang) 
                                MaterialTheme.colorScheme.primaryContainer 
                            else 
                                MaterialTheme.colorScheme.surface,
                            tonalElevation = 1.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Dil adı
                                Text(
                                    text = lang.displayName,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                
                                // Seçili göstergesi
                                if (language == lang) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = { }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Dil seçici butonu
        IconButton(
            onClick = { showLanguageDialog = true },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_language),
                contentDescription = text.selectLanguage
            )
        }

        // Logo ve Butonlar
        AnimatedAuthContent(
            showContent = showContent,
            onEmailSignIn = onEmailSignIn,
            onGoogleSignIn = onGoogleSignIn,
            onRegister = onRegister,
            text = text,
            viewModel = viewModel
        )

        // Alt bilgi metni
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            val privacyText = remember(language) { UiText.getPrivacyPolicyText(language) }
            
            Text(
                text = buildAnnotatedString {
                    append(text.byCreatingAccount)
                    append(" ")
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append(privacyText.privacyPolicy)
                    }
                    append(" ")
                    append(privacyText.and)
                    append(" ")
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append(privacyText.termsOfService)
                    }
                },
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clickable { onShowPrivacyPolicy() }
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
private fun AnimatedAuthContent(
    showContent: Boolean,
    onEmailSignIn: () -> Unit,
    onGoogleSignIn: () -> Unit,
    onRegister: () -> Unit,
    text: UiText.AuthText,
    viewModel: AppViewModel
) {
    AnimatedVisibility(
        visible = showContent,
        enter = fadeIn() + slideInVertically(initialOffsetY = { -40 })
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                with(LocalDensity.current) {
                    minOf(
                        16.dp,
                        (LocalConfiguration.current.screenHeightDp.dp * 0.02f)
                    )
                }
            )
        ) {
            Spacer(modifier = Modifier.weight(0.3f))

            // Logo
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(
                    with(LocalDensity.current) {
                        minOf(
                            100.dp,
                            LocalConfiguration.current.screenWidthDp.dp * 0.25f
                        )
                    }
                )
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_app_logo),
                        contentDescription = "App Logo",
                        modifier = Modifier.size(80.dp),
                        tint = Color.Unspecified
                    )
                }
            }

            // Başlık ve Alt Başlık
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = text.welcome,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = text.signInToAccount,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.weight(0.05f))

            // Butonlar
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // E-posta Butonu
                Button(
                    onClick = onEmailSignIn,
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .heightIn(min = 48.dp, max = 56.dp),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = text.email
                    )
                    Text(
                        text = text.signIn,
                        modifier = Modifier.padding(start = 8.dp),
                        fontSize = 16.sp
                    )
                }

                // Google Butonu
                OutlinedButton(
                    onClick = {
                        viewModel.prepareGoogleSignIn()
                        onGoogleSignIn()
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .heightIn(min = 48.dp, max = 56.dp),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.Unspecified
                    )
                    Text(
                        text = text.continueWithGoogle,
                        modifier = Modifier.padding(start = 8.dp),
                        fontSize = 16.sp
                    )
                }
            }

            // Google Butonu'ndan sonra ekleyelim
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    text = text.dontHaveAccount,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                
                TextButton(
                    onClick = onRegister,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = text.register,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.weight(0.3f))
        }
    }
} 