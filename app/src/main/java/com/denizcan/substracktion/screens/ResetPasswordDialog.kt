package com.denizcan.substracktion.screens

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextAlign
import com.denizcan.substracktion.util.Language
import com.denizcan.substracktion.util.UiText

@Composable
fun ResetPasswordDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    email: String,
    maskedEmail: String,
    language: Language
) {
    val text = remember(language) { UiText.getEmailSignInText(language) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = text.resetPassword)
        },
        text = {
            Text(
                text = text.resetPasswordEmailSent.format(maskedEmail),
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text.send)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text.cancel)
            }
        }
    )
} 