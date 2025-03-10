package com.denizcan.substracktion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.denizcan.substracktion.navigation.NavGraph
import com.denizcan.substracktion.viewmodel.AppViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import androidx.lifecycle.ViewModelProvider
import com.denizcan.substracktion.theme.SubstracktionTheme
import com.denizcan.substracktion.util.NotificationHelper
import com.google.firebase.messaging.FirebaseMessaging
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    private lateinit var appViewModel: AppViewModel

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account.idToken?.let { token ->
                    appViewModel.signInWithGoogle(token)
                }
            } catch (e: ApiException) {
                when (e.statusCode) {
                    GoogleSignInStatusCodes.SIGN_IN_CANCELLED -> {
                        // Kullanıcı iptal etti, hata göstermeye gerek yok
                    }
                    else -> {
                        appViewModel.setError("Google sign in failed: ${e.message}")
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Bildirim izinlerini kontrol et
        if (!NotificationHelper.checkNotificationPermission(this)) {
            NotificationHelper.requestNotificationPermission(this)
        }

        setContent {
            appViewModel = viewModel(
                factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            )

            // FCM token'ı al
            LaunchedEffect(Unit) {
                appViewModel.initGoogleSignIn(this@MainActivity)

                // FCM token'ı al
                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val token = task.result
                        appViewModel.saveFcmToken(token)
                        println("FCM Token alındı: $token")
                    } else {
                        println("FCM Token alınamadı: ${task.exception?.message}")
                    }
                }
            }

            SubstracktionTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavGraph(
                        navController = navController,
                        viewModel = appViewModel,
                        onGoogleSignInClick = {
                            googleSignInLauncher.launch(appViewModel.getGoogleSignInIntent())
                        }
                    )
                }
            }
        }
    }

    // İzin sonucunu işle
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            NotificationHelper.NOTIFICATION_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    println("Bildirim izni verildi")
                } else {
                    println("Bildirim izni reddedildi")
                }
            }
        }
    }
}