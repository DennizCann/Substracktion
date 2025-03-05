package com.denizcan.substracktion.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Auth : Screen("auth")
    object EmailSignIn : Screen("email_sign_in")
    object Register : Screen("register")
    object Home : Screen("home")
    object Subscriptions : Screen("subscriptions")
    object Calendar : Screen("calendar")
    object Analytics : Screen("analytics")
    // Diğer ekranları daha sonra buraya ekleyeceğiz
} 