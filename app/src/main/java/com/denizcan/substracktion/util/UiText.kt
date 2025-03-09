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
        val profile: String,
        val settings: String,
        val signOut: String,
        val menu: String,
        val notifications: String,
        val emailNotifications: String,
        val country: String,
        val currency: String,
        val deleteAccount: String,
        val privacyPolicy: String,
        val termsOfService: String
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
        val home: String,
        val subscriptions: String,
        val calendar: String,
        val analytics: String
    )

    data class UnderConstructionText(
        val comingSoon: String,
        val underConstruction: String,
        val backToHome: String
    )

    data class PrivacyPolicyText(
        val title: String,
        val content: String,
        val ok: String,
        val privacyPolicy: String,
        val termsOfService: String,
        val and: String
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

    data class ProfileText(
        val regionSettings: String,
        val region: String,
        val currency: String,
        val notificationSettings: String,
        val notifications: String,
        val emailNotifications: String,
        val appSettings: String,
        val languageSettings: String,
        val darkMode: String
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
                Language.ENGLISH -> DrawerText(
                    profile = "Profile",
                    settings = "Settings",
                    signOut = "Sign Out",
                    menu = "Menu",
                    notifications = "Notifications",
                    emailNotifications = "Email Notifications",
                    country = "Country",
                    currency = "Currency",
                    deleteAccount = "Delete Account",
                    privacyPolicy = "Privacy Policy",
                    termsOfService = "Terms of Service"
                )
                Language.TURKISH -> DrawerText(
                    profile = "Profil",
                    settings = "Ayarlar",
                    signOut = "Çıkış Yap",
                    menu = "Menü",
                    notifications = "Bildirimler",
                    emailNotifications = "E-posta Bildirimleri",
                    country = "Ülke",
                    currency = "Para Birimi",
                    deleteAccount = "Hesabı Sil",
                    privacyPolicy = "Gizlilik Politikası",
                    termsOfService = "Kullanım Koşulları"
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
                    home = "Home",
                    subscriptions = "Subscriptions",
                    calendar = "Calendar",
                    analytics = "Analytics"
                )
                Language.TURKISH -> BottomNavText(
                    home = "Ana Sayfa",
                    subscriptions = "Abonelikler",
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

        fun getPrivacyPolicyText(language: Language): PrivacyPolicyText {
            return when (language) {
                Language.ENGLISH -> PrivacyPolicyText(
                    title = "Privacy & Terms",
                    content = """
                        We only collect essential information:
                        • Email for authentication
                        • Subscription data you provide
                        
                        Your data is:
                        • Stored securely
                        • Never shared with third parties
                        • Only used to provide service
                        • Deletable upon request
                    """.trimIndent(),
                    ok = "OK",
                    privacyPolicy = "Privacy Policy",
                    termsOfService = "Terms of Service",
                    and = "and"
                )
                Language.TURKISH -> PrivacyPolicyText(
                    title = "Gizlilik ve Koşullar",
                    content = """
                        Sadece gerekli bilgileri topluyoruz:
                        • Kimlik doğrulama için e-posta
                        • Sizin girdiğiniz abonelik verileri
                        
                        Verileriniz:
                        • Güvenle saklanır
                        • Üçüncü taraflarla paylaşılmaz
                        • Sadece hizmet sunmak için kullanılır
                        • İsteğiniz üzerine silinebilir
                    """.trimIndent(),
                    ok = "Tamam",
                    privacyPolicy = "Gizlilik Politikası",
                    termsOfService = "Kullanım Koşulları",
                    and = "ve"
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

        fun getProfileText(language: Language): ProfileText {
            return when (language) {
                Language.TURKISH -> ProfileText(
                    regionSettings = "Bölge Ayarları",
                    region = "Bölge",
                    currency = "Para Birimi",
                    notificationSettings = "Bildirim Ayarları",
                    notifications = "Bildirimler",
                    emailNotifications = "E-posta Bildirimleri",
                    appSettings = "Uygulama Ayarları",
                    languageSettings = "Dil",
                    darkMode = "Karanlık Mod"
                )
                Language.ENGLISH -> ProfileText(
                    regionSettings = "Region Settings",
                    region = "Region",
                    currency = "Currency",
                    notificationSettings = "Notification Settings",
                    notifications = "Notifications",
                    emailNotifications = "Email Notifications",
                    appSettings = "App Settings",
                    languageSettings = "Language",
                    darkMode = "Dark Mode"
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