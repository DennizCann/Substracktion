package com.denizcan.substracktion.util

sealed class UiText {
    data class AuthText(
        val signIn: String,
        val back: String,
        val welcome: String,
        val signInToAccount: String,
        val email: String,
        val password: String,
        val forgotPassword: String,
        val dontHaveAccount: String,
        val register: String,
        val createAccount: String,
        val welcomeToApp: String,
        val fullName: String,
        val confirmPassword: String,
        val termsOfService: String,
        val selectLanguage: String,
        val continueWithGoogle: String,
        val byCreatingAccount: String
    )

    data class EmailSignInText(
        val signIn: String,
        val back: String,
        val welcome: String,
        val signInToAccount: String,
        val email: String,
        val password: String,
        val forgotPassword: String,
        val register: String,
        val dontHaveAccount: String,
        val continueWithGoogle: String,
        val resetPassword: String,
        val resetPasswordSuccess: String,
        val resetPasswordEmailSent: String,
        val send: String,
        val cancel: String,
        val ok: String
    )

    data class RegisterText(
        val createAccount: String,
        val welcomeToApp: String,
        val fullName: String,
        val email: String,
        val password: String,
        val confirmPassword: String,
        val register: String,
        val termsOfService: String,
        val back: String
    )

    data class HomeText(
        val welcome: String,
        val signOut: String,
        val homeTitle: String
    )

    data class DrawerText(
        val home: String,
        val subscriptions: String,
        val calendar: String,
        val analytics: String,
        val profile: String,
        val signOut: String,
        val country: String,
        val currency: String,
        val language: String,
        val deleteAccount: String
    )

    data class WelcomeText(
        val slogan: String,
        val getStarted: String,
        val features: List<Feature>
    ) {
        data class Feature(
            val title: String,
            val description: String
        )
    }

    data class BottomNavText(
        val subscriptions: String,
        val add: String,
        val calendar: String,
        val analytics: String
    )

    data class UnderConstructionText(
        val comingSoon: String,
        val underConstruction: String,
        val backToHome: String
    )

    data class NotificationsText(
        val title: String,
        val noNotifications: String,
        val markAllRead: String,
        val justNow: String,
        val minutesAgo: String,
        val hoursAgo: String,
        val daysAgo: String
    )

    data class CommonText(
        val back: String,
        val cancel: String,
        val save: String,
        val delete: String,
        val edit: String,
        val ok: String
    )

    companion object {
        fun getAuthText(language: Language): AuthText {
            return when (language) {
                Language.ENGLISH -> AuthText(
                    signIn = "Sign In",
                    back = "Back",
                    welcome = "Welcome!",
                    signInToAccount = "Sign in to your account",
                    email = "Email",
                    password = "Password",
                    forgotPassword = "Forgot Password?",
                    dontHaveAccount = "Don't have an account?",
                    register = "Register",
                    createAccount = "Create Account",
                    welcomeToApp = "Welcome to Substracktion",
                    fullName = "Full Name",
                    confirmPassword = "Confirm Password",
                    termsOfService = "By creating an account, you agree to our Terms of Service",
                    selectLanguage = "Select Language",
                    continueWithGoogle = "Continue with Google",
                    byCreatingAccount = "By creating an account, you agree to our"
                )
                Language.TURKISH -> AuthText(
                    signIn = "Giriş Yap",
                    back = "Geri",
                    welcome = "Hoş Geldiniz!",
                    signInToAccount = "Hesabınıza giriş yapın",
                    email = "E-posta",
                    password = "Şifre",
                    forgotPassword = "Şifremi Unuttum",
                    dontHaveAccount = "Hesabınız yok mu?",
                    register = "Kayıt Ol",
                    createAccount = "Hesap Oluştur",
                    welcomeToApp = "Substracktion'a hoş geldiniz",
                    fullName = "Ad Soyad",
                    confirmPassword = "Şifre Tekrar",
                    termsOfService = "Hesap oluşturarak kullanım koşullarını kabul etmiş olursunuz",
                    selectLanguage = "Dil Seçin",
                    continueWithGoogle = "Google ile devam et",
                    byCreatingAccount = "Hesap oluşturarak kabul etmiş olursunuz:"
                )
            }
        }

        fun getEmailSignInText(language: Language): EmailSignInText {
            return when (language) {
                Language.ENGLISH -> EmailSignInText(
                    signIn = "Sign In",
                    back = "Back",
                    welcome = "Welcome!",
                    signInToAccount = "Sign in to your account",
                    email = "Email",
                    password = "Password",
                    forgotPassword = "Forgot Password?",
                    register = "Register",
                    dontHaveAccount = "Don't have an account?",
                    continueWithGoogle = "Continue with Google",
                    resetPassword = "Reset Password",
                    resetPasswordSuccess = "Password Reset",
                    resetPasswordEmailSent = "We'll send a password reset link to:\n%s",
                    send = "Send",
                    cancel = "Cancel",
                    ok = "OK"
                )
                Language.TURKISH -> EmailSignInText(
                    signIn = "Giriş Yap",
                    back = "Geri",
                    welcome = "Hoş Geldiniz!",
                    signInToAccount = "Hesabınıza giriş yapın",
                    email = "E-posta",
                    password = "Şifre",
                    forgotPassword = "Şifremi Unuttum",
                    register = "Kayıt Ol",
                    dontHaveAccount = "Hesabınız yok mu?",
                    continueWithGoogle = "Google ile devam et",
                    resetPassword = "Şifre Sıfırla",
                    resetPasswordSuccess = "Şifre Sıfırlama",
                    resetPasswordEmailSent = "Şifre sıfırlama bağlantısını şu adrese göndereceğiz:\n%s",
                    send = "Gönder",
                    cancel = "İptal",
                    ok = "Tamam"
                )
            }
        }

        fun getRegisterText(language: Language): RegisterText {
            return when (language) {
                Language.ENGLISH -> RegisterText(
                    createAccount = "Create Account",
                    welcomeToApp = "Welcome to Substracktion",
                    fullName = "Full Name",
                    email = "Email",
                    password = "Password",
                    confirmPassword = "Confirm Password",
                    register = "Register",
                    termsOfService = "By creating an account, you agree to our Terms of Service",
                    back = "Back"
                )
                Language.TURKISH -> RegisterText(
                    createAccount = "Hesap Oluştur",
                    welcomeToApp = "Substracktion'a hoş geldiniz",
                    fullName = "Ad Soyad",
                    email = "E-posta",
                    password = "Şifre",
                    confirmPassword = "Şifre Tekrar",
                    register = "Kayıt Ol",
                    termsOfService = "Hesap oluşturarak kullanım koşullarını kabul etmiş olursunuz",
                    back = "Geri"
                )
            }
        }

        fun getHomeText(language: Language): HomeText {
            return when (language) {
                Language.ENGLISH -> HomeText(
                    welcome = "Welcome, ",
                    signOut = "Sign Out",
                    homeTitle = "Home"
                )
                Language.TURKISH -> HomeText(
                    welcome = "Hoş Geldin, ",
                    signOut = "Çıkış Yap",
                    homeTitle = "Ana Sayfa"
                )
            }
        }

        fun getDrawerText(language: Language): DrawerText {
            return when (language) {
                Language.TURKISH -> DrawerText(
                    home = "Ana Sayfa",
                    subscriptions = "Abonelikler",
                    calendar = "Takvim",
                    analytics = "Analiz",
                    profile = "Profil ve Ayarlar",
                    signOut = "Çıkış Yap",
                    country = "Ülke",
                    currency = "Para Birimi",
                    language = "Dil",
                    deleteAccount = "Hesabı Sil"
                )
                Language.ENGLISH -> DrawerText(
                    home = "Home",
                    subscriptions = "Subscriptions",
                    calendar = "Calendar",
                    analytics = "Analytics",
                    profile = "Profile & Settings",
                    signOut = "Sign Out",
                    country = "Country",
                    currency = "Currency",
                    language = "Language",
                    deleteAccount = "Delete Account"
                )
            }
        }

        fun getWelcomeText(language: Language): WelcomeText {
            return when (language) {
                Language.ENGLISH -> WelcomeText(
                    slogan = "Manage Your Subscriptions Smartly",
                    getStarted = "Get Started",
                    features = listOf(
                        WelcomeText.Feature(
                            title = "📊 Detailed Tracking",
                            description = "Track and manage all your subscriptions easily in one place"
                        ),
                        WelcomeText.Feature(
                            title = "🔔 Smart Reminders",
                            description = "Never miss important payments with timely notifications"
                        ),
                        WelcomeText.Feature(
                            title = "💰 Cost Control",
                            description = "Analyze your expenses and view category-based reports"
                        ),
                        WelcomeText.Feature(
                            title = "📈 Financial Insights",
                            description = "Monitor your monthly and yearly spending trends to find savings opportunities"
                        )
                    )
                )
                Language.TURKISH -> WelcomeText(
                    slogan = "Aboneliklerinizi Akıllıca Yönetin",
                    getStarted = "Başlayalım",
                    features = listOf(
                        WelcomeText.Feature(
                            title = "📊 Detaylı Takip",
                            description = "Tüm aboneliklerinizi tek bir yerden kolayca takip edin ve yönetin"
                        ),
                        WelcomeText.Feature(
                            title = "🔔 Akıllı Hatırlatıcılar",
                            description = "Zamanında bildirimlerle önemli ödemeleri asla kaçırmayın"
                        ),
                        WelcomeText.Feature(
                            title = "💰 Maliyet Kontrolü",
                            description = "Harcamalarınızı analiz edin ve kategori bazlı raporları görüntüleyin"
                        ),
                        WelcomeText.Feature(
                            title = "📈 Finansal İçgörüler",
                            description = "Tasarruf fırsatlarını bulmak için aylık ve yıllık harcama trendlerinizi izleyin"
                        )
                    )
                )
            }
        }

        fun getBottomNavText(language: Language): BottomNavText {
            return when (language) {
                Language.ENGLISH -> BottomNavText(
                    subscriptions = "Subscriptions",
                    add = "Add",
                    calendar = "Calendar",
                    analytics = "Analytics"
                )
                Language.TURKISH -> BottomNavText(
                    subscriptions = "Üyelikler",
                    add = "Ekle",
                    calendar = "Takvim",
                    analytics = "Analiz"
                )
            }
        }

        fun getUnderConstructionText(language: Language): UnderConstructionText {
            return when (language) {
                Language.ENGLISH -> UnderConstructionText(
                    comingSoon = "Coming Soon",
                    underConstruction = "This page is under construction",
                    backToHome = "Back to Home"
                )
                Language.TURKISH -> UnderConstructionText(
                    comingSoon = "Çok Yakında",
                    underConstruction = "Bu sayfa yapım aşamasında",
                    backToHome = "Ana Sayfaya Dön"
                )
            }
        }

        fun getNotificationsText(language: Language): NotificationsText {
            return when (language) {
                Language.ENGLISH -> NotificationsText(
                    title = "Notifications",
                    noNotifications = "No notifications",
                    markAllRead = "Mark all as read",
                    justNow = "Just now",
                    minutesAgo = "%d minutes ago",
                    hoursAgo = "%d hours ago",
                    daysAgo = "%d days ago"
                )
                Language.TURKISH -> NotificationsText(
                    title = "Bildirimler",
                    noNotifications = "Bildirim bulunmuyor",
                    markAllRead = "Tümünü okundu işaretle",
                    justNow = "Şimdi",
                    minutesAgo = "%d dakika önce",
                    hoursAgo = "%d saat önce",
                    daysAgo = "%d gün önce"
                )
            }
        }

        fun getCommonText(language: Language): CommonText {
            return when (language) {
                Language.ENGLISH -> CommonText(
                    back = "Back",
                    cancel = "Cancel",
                    save = "Save",
                    delete = "Delete",
                    edit = "Edit",
                    ok = "OK"
                )
                Language.TURKISH -> CommonText(
                    back = "Geri",
                    cancel = "İptal",
                    save = "Kaydet",
                    delete = "Sil",
                    edit = "Düzenle",
                    ok = "Tamam"
                )
            }
        }
    }
} 