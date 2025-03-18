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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.denizcan.substracktion.viewmodel.ProfileViewModelFactory
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.navigation.NavType
import com.denizcan.substracktion.util.Language
import com.denizcan.substracktion.screens.SubscriptionsScreen
import com.denizcan.substracktion.screens.ServiceDetailScreen
import androidx.navigation.navArgument
import com.denizcan.substracktion.navigation.Screen  // Screen sınıfını import et

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Welcome.route,
    viewModel: AppViewModel,
    onGoogleSignInClick: () -> Unit
) {
    // State'leri en üstte collect edelim
    val language = viewModel.language.value
    val isLoading = viewModel.isLoading.collectAsState()
    val error = viewModel.error.collectAsState()
    val isUserSignedIn = viewModel.isUserSignedIn.collectAsState()
    val loginSuccess = viewModel.loginSuccess.collectAsState()
    val userName = viewModel.userName.collectAsState()  // userName'i burada collect edelim

    // Kullanıcı giriş yapmışsa direkt ana ekrana yönlendir
    LaunchedEffect(isUserSignedIn.value) {
        if (isUserSignedIn.value) {
            navController.navigate(Screen.Home.route) {  // Subscriptions -> Home
                popUpTo(Screen.Welcome.route) { inclusive = true }
            }
        }
    }

    // Başarılı giriş/kayıt sonrası ana ekrana yönlendir
    LaunchedEffect(loginSuccess.value) {
        if (loginSuccess.value) {
            navController.navigate(Screen.Home.route) {  // Subscriptions -> Home
                popUpTo(Screen.Welcome.route) { inclusive = true }
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
                language = language,
                onLanguageChange = { viewModel.updateLanguage(it) }
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
                onPrivacyPolicyClick = {
                    showPrivacyPolicy = true  // Dialog'u göster
                },
                language = language
            )

            // Gizlilik Politikası Dialog'u
            if (showPrivacyPolicy) {
                AlertDialog(
                    onDismissRequest = { showPrivacyPolicy = false },
                    title = {
                        Text(
                            if (language == Language.TURKISH)
                                "Gizlilik Politikası ve Kullanım Koşulları"
                            else
                                "Privacy Policy and Terms of Service"
                        )
                    },
                    text = {
                        Text(
                            if (language == Language.TURKISH)
                                "Bu uygulama, aboneliklerinizi takip etmenize yardımcı olmak için tasarlanmıştır. " +
                                "Topladığımız veriler:\n\n" +
                                "• E-posta adresiniz ve adınız (kimlik doğrulama için)\n" +
                                "• Abonelik bilgileriniz (ücret, tarih, kategori)\n" +
                                "• Uygulama tercihleri (dil, bildirim ayarları)\n\n" +
                                "Verileriniz:\n" +
                                "• Sadece sizin görüntülemeniz için saklanır\n" +
                                "• Üçüncü taraflarla paylaşılmaz\n" +
                                "• Google Firebase altyapısında güvenle depolanır\n" +
                                "• İstediğiniz zaman hesabınızı ve tüm verilerinizi silebilirsiniz\n\n" +
                                "Uygulamayı kullanarak bu koşulları kabul etmiş olursunuz."
                            else
                                "This application is designed to help you track your subscriptions. " +
                                "Data we collect:\n\n" +
                                "• Your email and name (for authentication)\n" +
                                "• Subscription information (cost, date, category)\n" +
                                "• App preferences (language, notification settings)\n\n" +
                                "Your data:\n" +
                                "• Is stored for your viewing only\n" +
                                "• Is not shared with third parties\n" +
                                "• Is securely stored on Google Firebase infrastructure\n" +
                                "• Can be deleted along with your account at any time\n\n" +
                                "By using the application, you accept these terms."
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = { showPrivacyPolicy = false }) {
                            Text(if (language == Language.TURKISH) "Tamam" else "OK")
                        }
                    }
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
            val verificationEmailSent = viewModel.verificationEmailSent.collectAsState()

            RegisterScreen(
                onRegister = { name, email, password ->
                    viewModel.register(name, email, password)
                },
                onBackClick = {
                    navController.navigateUp()
                },
                onRegistrationSuccess = {
                    // State'i temizle ve giriş ekranına yönlendir
                    viewModel.clearVerificationState()
                    navController.navigate(Screen.EmailSignIn.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                language = language,
                isLoading = isLoading.value,
                error = error.value,
                verificationEmailSent = verificationEmailSent.value
            )
        }

        // Ana sayfa (navigasyon için)
        composable(Screen.Home.route) {
            HomeScreen(
                onSignOut = {
                    viewModel.signOut()
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                userName = userName.value,
                language = language,
                onNavigate = { route ->
                    navController.navigate(route) {
                        // Geri tuşuna basıldığında Home'a dön
                        popUpTo(Screen.Home.route)
                    }
                }
            )
        }

        // Üyelikler listesi sayfası
        composable(Screen.Subscriptions.route) {
            SubscriptionsScreen(
                onBackToHome = { navController.navigateUp() },
                onServiceClick = { serviceId ->
                    navController.navigate(Screen.ServiceDetail.createRoute(serviceId))
                },
                language = language
            )
        }

        // Servis detay sayfası
        composable(
            route = Screen.ServiceDetail.route,
            arguments = listOf(
                navArgument("serviceId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getString("serviceId") ?: return@composable
            ServiceDetailScreen(
                serviceId = serviceId,
                onBack = { navController.navigateUp() }
            )
        }

        // Yeni üyelik ekleme sayfası
        composable(Screen.Add.route) {
            AddSubscriptionScreen(
                onBackToHome = { navController.navigateUp() },
                language = language
            )
        }

        // Analiz sayfası
        composable(Screen.Analytics.route) {
            AnalyticsScreen(
                onBackToHome = { navController.navigateUp() },
                language = language
            )
        }

        // Takvim sayfası
        composable(Screen.Calendar.route) {
            CalendarScreen(
                onBackToHome = { navController.navigateUp() },
                language = language
            )
        }

        // Profil ve Ayarlar sayfaları için route'lar
        composable(Screen.Profile.route) {
            ProfileScreen(
                onBackToHome = { navController.navigateUp() },
                viewModel = viewModel(factory = ProfileViewModelFactory()),
                language = language,
                onLanguageChange = { viewModel.updateLanguage(it) },
                onDeleteAccountClick = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
} 