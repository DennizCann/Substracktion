package com.denizcan.substracktion.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Auth : Screen("auth")
    object EmailSignIn : Screen("email_sign_in")
    object Register : Screen("register")
    object Home : Screen("home")
    object Subscriptions : Screen("subscriptions")
    object Add : Screen("add")
    object Analytics : Screen("analytics")
    object Calendar : Screen("calendar")
    object Profile : Screen("profile")
    object ServiceDetail : Screen("service_detail/{serviceId}") {
        fun createRoute(serviceId: String) = "service_detail/$serviceId"
    }
    // Diğer ekranları daha sonra buraya ekleyeceğiz
} 