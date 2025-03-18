package com.denizcan.substracktion.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.denizcan.substracktion.R
import com.denizcan.substracktion.util.EmailValidator
import com.denizcan.substracktion.util.Language
import com.denizcan.substracktion.util.UiText
import com.denizcan.substracktion.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailSignInScreen(
    onSignIn: (email: String, password: String) -> Unit,
    onResetPassword: (email: String) -> Unit,
    onNavigateToRegister: () -> Unit,
    onBackClick: () -> Unit,
    language: Language,
    isLoading: Boolean,
    error: String?,
    resetPasswordSuccess: Boolean,
    viewModel: AppViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var isResetEmailValid by remember { mutableStateOf(true) }
    var resetEmail by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val text = remember(language) { UiText.getEmailSignInText(language) }

    // Başarılı şifre sıfırlama durumunda success dialog'u göster
    LaunchedEffect(resetPasswordSuccess) {
        if (resetPasswordSuccess) {
            showSuccessDialog = true
        }
    }

    // Şifre sıfırlama başarılı dialog'u
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { 
                showSuccessDialog = false
                viewModel.clearResetPasswordSuccess()
            },
            title = { Text(text.resetPasswordSuccess) },
            text = { 
                Text(
                    text = text.resetPasswordEmailSent.format(resetEmail),
                    textAlign = TextAlign.Center
                ) 
            },
            confirmButton = {
                TextButton(
                    onClick = { 
                        showSuccessDialog = false
                        viewModel.clearResetPasswordSuccess()
                    }
                ) {
                    Text(text.ok)
                }
            }
        )
    }

    // Şifre sıfırlama dialog'u
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { 
                showResetDialog = false 
                resetEmail = ""
                isResetEmailValid = true
            },
            title = { Text(text.resetPassword) },
            text = {
                OutlinedTextField(
                    value = resetEmail,
                    onValueChange = { 
                        resetEmail = it
                        isResetEmailValid = EmailValidator.isValid(resetEmail)
                    },
                    label = { Text(text.email) },
                    singleLine = true,
                    isError = !isResetEmailValid && resetEmail.isNotEmpty(),
                    supportingText = {
                        if (!isResetEmailValid && resetEmail.isNotEmpty()) {
                            Text(
                                text = if (language == Language.TURKISH)
                                    "Geçerli bir e-posta adresi giriniz"
                                else
                                    "Please enter a valid email address",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onResetPassword(resetEmail)
                        showResetDialog = false
                    },
                    enabled = resetEmail.isNotEmpty() && isResetEmailValid
                ) {
                    Text(text.send)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showResetDialog = false
                        resetEmail = ""
                        isResetEmailValid = true
                    }
                ) {
                    Text(text.cancel)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text.signIn) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = text.back
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -40 })
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = text.welcome,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Text(
                        text = text.signInToAccount,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(text.email) },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = text.email
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(text.password) },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = text.password
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (passwordVisible) "Hide Password" else "Show Password"
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { onSignIn(email, password) }
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Şifremi unuttum butonu
                    TextButton(
                        onClick = { showResetDialog = true },
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text(text.forgotPassword)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Loading göstergesi
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }

                    // Hata mesajı
                    error?.let { errorMessage ->
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    Button(
                        onClick = { onSignIn(email, password) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = MaterialTheme.shapes.medium,
                        enabled = !isLoading && email.isNotEmpty() && password.isNotEmpty()
                    ) {
                        Text(text.signIn)
                    }

                    // Kayıt ol butonu ekleyelim
                    TextButton(
                        onClick = onNavigateToRegister,
                        enabled = !isLoading
                    ) {
                        Text(
                            if (language == Language.TURKISH)
                                "Hesabınız yok mu? Kayıt olun"
                            else
                                "Don't have an account? Sign up"
                        )
                    }
                }
            }
        }
    }
} 