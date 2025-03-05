package com.denizcan.substracktion.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    
    var selectedTab by remember { mutableStateOf(Screen.Home.route) }
    var showNotifications by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(200.dp),
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
                    icon = { Icon(Icons.Default.Person, contentDescription = drawerText.profile) },
                    label = { Text(drawerText.profile) },
                    selected = false,
                    onClick = { 
                        scope.launch {
                            drawerState.close()
                            onNavigate(Screen.Profile.route)
                        }
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = drawerText.settings) },
                    label = { Text(drawerText.settings) },
                    selected = false,
                    onClick = { 
                        scope.launch {
                            drawerState.close()
                            onNavigate(Screen.Settings.route)
                        }
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.ExitToApp, contentDescription = drawerText.signOut) },
                    label = { Text(drawerText.signOut) },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            onSignOut()
                        }
                    }
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
                                contentDescription = drawerText.menu
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { showNotifications = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = drawerText.notifications
                            )
                        }
                        
                        NotificationsMenu(
                            expanded = showNotifications,
                            onDismiss = { showNotifications = false },
                            notifications = emptyList(), // Şimdilik boş liste
                            onMarkAllRead = { /* Tümünü okundu işaretle */ },
                            language = language
                        )
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = selectedTab == Screen.Home.route,
                        onClick = { 
                            selectedTab = Screen.Home.route
                            onNavigate(Screen.Home.route)
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = bottomNavText.home) },
                        label = { Text(bottomNavText.home) }
                    )
                    NavigationBarItem(
                        selected = selectedTab == Screen.Subscriptions.route,
                        onClick = { 
                            selectedTab = Screen.Subscriptions.route
                            onNavigate(Screen.Subscriptions.route)
                        },
                        icon = { Icon(Icons.Default.Subscriptions, contentDescription = bottomNavText.subscriptions) },
                        label = { Text(bottomNavText.subscriptions) }
                    )
                    NavigationBarItem(
                        selected = selectedTab == Screen.Calendar.route,
                        onClick = { 
                            selectedTab = Screen.Calendar.route
                            onNavigate(Screen.Calendar.route)
                        },
                        icon = { Icon(Icons.Default.CalendarMonth, contentDescription = bottomNavText.calendar) },
                        label = { Text(bottomNavText.calendar) }
                    )
                    NavigationBarItem(
                        selected = selectedTab == Screen.Analytics.route,
                        onClick = { 
                            selectedTab = Screen.Analytics.route
                            onNavigate(Screen.Analytics.route)
                        },
                        icon = { Icon(Icons.Default.Analytics, contentDescription = bottomNavText.analytics) },
                        label = { Text(bottomNavText.analytics) }
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // İçerik buraya gelecek
            }
        }
    }
} 