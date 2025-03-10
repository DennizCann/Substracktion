package com.denizcan.substracktion.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.denizcan.substracktion.R
import androidx.compose.material3.TextButton
import androidx.compose.ui.text.withStyle
import com.denizcan.substracktion.util.Language
import com.denizcan.substracktion.util.UiText

@Composable
fun AuthScreen(
    onEmailSignIn: () -> Unit,
    onGoogleSignIn: () -> Unit,
    onRegister: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    language: Language
) {
    val authText = remember(language) { UiText.getAuthText(language) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(100.dp)
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

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = authText.welcome,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = authText.signInToAccount,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // E-posta ile Giriş Butonu
        Button(
            onClick = onEmailSignIn,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(authText.signIn)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Google ile Giriş Butonu
        OutlinedButton(
            onClick = onGoogleSignIn,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 50.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(authText.continueWithGoogle)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Kayıt Ol Butonu
        TextButton(
            onClick = onRegister
        ) {
            Text(
                buildAnnotatedString {
                    append(authText.dontHaveAccount)
                    append(" ")
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append(authText.register)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Gizlilik Politikası Linki
        Text(
            text = buildAnnotatedString {
                append(authText.byCreatingAccount)
                append(" ")
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append(authText.termsOfService)
                }
            },
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clickable(onClick = onPrivacyPolicyClick)
                .padding(horizontal = 16.dp)
        )
    }
}

