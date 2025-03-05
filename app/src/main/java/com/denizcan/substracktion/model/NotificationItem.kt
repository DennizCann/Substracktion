package com.denizcan.substracktion.model

import java.util.Date

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: Date,
    val isRead: Boolean = false,
    val type: NotificationType = NotificationType.INFO
)

enum class NotificationType {
    INFO,
    WARNING,
    SUCCESS
} 