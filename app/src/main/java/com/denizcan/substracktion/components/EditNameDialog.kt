package com.denizcan.substracktion.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.denizcan.substracktion.util.Language

@Composable
fun EditNameDialog(
    currentName: String,
    onDismiss: () -> Unit,
    onNameChange: (String) -> Unit,
    language: Language
) {
    var name by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                if (language == Language.TURKISH) 
                    "İsminizi Düzenleyin" 
                else 
                    "Edit Your Name"
            )
        },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { 
                    Text(
                        if (language == Language.TURKISH) 
                            "İsim" 
                        else 
                            "Name"
                    )
                }
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        onNameChange(name)
                        onDismiss()
                    }
                }
            ) {
                Text(if (language == Language.TURKISH) "Kaydet" else "Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (language == Language.TURKISH) "İptal" else "Cancel")
            }
        }
    )
} 