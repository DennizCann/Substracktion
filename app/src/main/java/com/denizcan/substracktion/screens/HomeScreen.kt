package com.denizcan.substracktion.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.denizcan.substracktion.navigation.Screen
import com.denizcan.substracktion.util.Language
import com.denizcan.substracktion.util.UiText
import kotlinx.coroutines.launch
import com.denizcan.substracktion.components.NotificationsMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSignOut: () -> Unit,
    userName: String?,
    language: Language,
    onNavigate: (String) -> Unit
) {
    val drawerText = remember(language) { UiText.getDrawerText(language) }
    val homeText = remember(language) { UiText.getHomeText(language) }
    val bottomNavText = remember(language) { UiText.getBottomNavText(language) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    var selectedTab by rememberSaveable { mutableStateOf(Screen.Home.route) }
    var showNotifications by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp),
                drawerContainerColor = MaterialTheme.colorScheme.surface,
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = userName ?: "",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                
                NavigationDrawerItem(
                    label = { 
                        Text(
                            text = drawerText.profile,
                            maxLines = 1
                        )
                    },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            onNavigate(Screen.Profile.route)
                        }
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) }
                )
                NavigationDrawerItem(
                    label = { Text(drawerText.signOut) },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            onSignOut()
                        }
                    },
                    icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { 
                        Text(
                            text = homeText.homeTitle,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = if (language == Language.TURKISH) "Menü" else "Menu"
                            )
                        }
                    },
                    actions = {
                        Box {
                            IconButton(
                                onClick = { showNotifications = true }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = if (language == Language.TURKISH) "Bildirimler" else "Notifications"
                                )
                            }
                            
                            NotificationsMenu(
                                expanded = showNotifications,
                                onDismiss = { showNotifications = false },
                                notifications = emptyList(),
                                onMarkAllRead = { /* Tümünü okundu işaretle */ },
                                language = language
                            )
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    listOf(
                        Triple(Screen.Add.route, Icons.Default.Add, bottomNavText.add),
                        Triple(Screen.Subscriptions.route, Icons.Default.ViewList, bottomNavText.subscriptions),
                        Triple(Screen.Analytics.route, Icons.Default.Analytics, bottomNavText.analytics),
                        Triple(Screen.Calendar.route, Icons.Default.CalendarMonth, bottomNavText.calendar)
                    ).forEach { (route, icon, label) ->
                        NavigationBarItem(
                            selected = selectedTab == route,
                            onClick = { 
                                if (selectedTab != route) {
                                    selectedTab = route
                                    onNavigate(route)
                                }
                            },
                            icon = { Icon(icon, contentDescription = label) },
                            label = { Text(label) }
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Ana sayfa boş olacak, sadece navigasyon için kullanılacak
            }
        }
    }
}

@Composable
private fun SubscriptionsContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Başlık
        Text(
            text = "Aktif Üyelikler",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Geçici olarak örnek üyelikler
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            ListItem(
                headlineContent = { Text("Netflix") },
                supportingContent = { Text("Premium Plan") },
                trailingContent = { Text("₺89.99/ay") },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            ListItem(
                headlineContent = { Text("Spotify") },
                supportingContent = { Text("Aile Paketi") },
                trailingContent = { Text("₺49.99/ay") },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
        }

        // Toplam tutar
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            ListItem(
                headlineContent = { 
                    Text(
                        "Toplam Aylık",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                trailingContent = { 
                    Text(
                        "₺139.98",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            )
        }
    }
}
