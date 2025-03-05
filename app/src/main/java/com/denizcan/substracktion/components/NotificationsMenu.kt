package com.denizcan.substracktion.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.denizcan.substracktion.model.NotificationItem
import com.denizcan.substracktion.util.Language
import com.denizcan.substracktion.util.UiText
import com.denizcan.substracktion.util.UiText.NotificationsText
import java.util.*

@Composable
fun NotificationsMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    notifications: List<NotificationItem>,
    onMarkAllRead: () -> Unit,
    language: Language
) {
    val text = remember(language) { UiText.getNotificationsText(language) }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = Modifier
            .width(300.dp)
            .heightIn(max = 400.dp)
    ) {
        Column {
            // Başlık ve "Tümünü Okundu İşaretle" butonu
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text.title,
                    style = MaterialTheme.typography.titleMedium
                )
                if (notifications.isNotEmpty()) {
                    TextButton(onClick = onMarkAllRead) {
                        Text(text.markAllRead)
                    }
                }
            }
            
            Divider()
            
            if (notifications.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text.noNotifications)
                }
            } else {
                LazyColumn {
                    items(notifications) { notification ->
                        NotificationItem(
                            notification = notification,
                            language = language
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationItem(
    notification: NotificationItem,
    language: Language
) {
    val text = remember(language) { UiText.getNotificationsText(language) }
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = if (!notification.isRead) 
            MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        else 
            MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = notification.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notification.message,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = getRelativeTimeString(notification.timestamp, text),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

private fun getRelativeTimeString(date: Date, text: NotificationsText): String {
    val now = Date()
    val diff = now.time - date.time
    val minutes = diff / (60 * 1000)
    val hours = minutes / 60
    val days = hours / 24

    return when {
        minutes < 1 -> text.justNow
        hours < 1 -> text.minutesAgo.format(minutes)
        days < 1 -> text.hoursAgo.format(hours)
        else -> text.daysAgo.format(days)
    }
} 