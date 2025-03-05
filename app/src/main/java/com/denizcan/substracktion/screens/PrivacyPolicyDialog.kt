package com.denizcan.substracktion.screens

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.denizcan.substracktion.util.Language
import com.denizcan.substracktion.util.UiText

@Composable
fun PrivacyPolicyDialog(
    onDismiss: () -> Unit,
    language: Language
) {
    val text = remember(language) { UiText.getPrivacyPolicyText(language) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = text.title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = text.content,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text.ok)
            }
        }
    )
}