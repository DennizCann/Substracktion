package com.denizcan.substracktion.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.denizcan.substracktion.screens.*
import com.denizcan.substracktion.viewmodel.AppViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Welcome.route,
    viewModel: AppViewModel,
    onGoogleSignInClick: () -> Unit
) {
    val language = viewModel.language.value
    val isLoading = viewModel.isLoading.collectAsState()
    val error = viewModel.error.collectAsState()
    val isUserSignedIn = viewModel.isUserSignedIn.collectAsState()
    val loginSuccess = viewModel.loginSuccess.collectAsState()

    // Kullanıcı giriş yapmışsa direkt ana ekrana yönlendir
    LaunchedEffect(isUserSignedIn.value) {
        if (isUserSignedIn.value) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Welcome.route) { inclusive = true }
            }
        }
    }

    // Başarılı giriş/kayıt sonrası ana ekrana yönlendir
    LaunchedEffect(loginSuccess.value) {
        if (loginSuccess.value) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Auth.route) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onNavigateToNext = {
                    navController.navigate(Screen.Auth.route)
                },
                language = language
            )
        }

        composable(Screen.Auth.route) {
            var showPrivacyPolicy by rememberSaveable { mutableStateOf(false) }

            AuthScreen(
                onEmailSignIn = {
                    navController.navigate(Screen.EmailSignIn.route)
                },
                onGoogleSignIn = onGoogleSignInClick,
                onRegister = {
                    navController.navigate(Screen.Register.route)
                },
                language = language,
                onLanguageChange = { viewModel.setLanguage(it) },
                viewModel = viewModel,
                onShowPrivacyPolicy = { showPrivacyPolicy = true }
            )

            if (showPrivacyPolicy) {
                PrivacyPolicyDialog(
                    onDismiss = { showPrivacyPolicy = false },
                    language = language
                )
            }
        }

        composable(Screen.EmailSignIn.route) {
            val resetPasswordSuccess = viewModel.resetPasswordSuccess.collectAsState()
            EmailSignInScreen(
                onSignIn = { email, password ->
                    viewModel.signInWithEmail(email, password)
                },
                onResetPassword = { email ->
                    viewModel.resetPassword(email)
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onBackClick = {
                    navController.navigateUp()
                },
                language = language,
                isLoading = isLoading.value,
                error = error.value,
                resetPasswordSuccess = resetPasswordSuccess.value,
                viewModel = viewModel
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegister = { name, email, password ->
                    viewModel.register(name, email, password)
                },
                onBackClick = {
                    navController.navigateUp()
                },
                language = language,
                isLoading = isLoading.value,
                error = error.value
            )
        }

        composable(Screen.Home.route) {
            val userName = viewModel.userName.collectAsState()
            HomeScreen(
                onSignOut = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                    viewModel.signOut()
                },
                userName = userName.value,
                language = language,
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        // Yeni sayfalar için route'lar
        composable(Screen.Subscriptions.route) {
            SubscriptionsScreen(
                onBackToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                language = language
            )
        }

        composable(Screen.Calendar.route) {
            CalendarScreen(
                onBackToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                language = language
            )
        }

        composable(Screen.Analytics.route) {
            AnalyticsScreen(
                onBackToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                language = language
            )
        }

        // Profil ve Ayarlar sayfaları için route'lar
        composable(Screen.Profile.route) {
            ProfileScreen(
                onBackToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                language = language
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                language = language
            )
        }
    }
} 