package com.denizcan.substracktion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
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
import com.denizcan.substracktion.screens.theme.SubstracktionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SubstracktionTheme {  // Şimdilik varsayılan tema ayarlarını kullanalım
                val appViewModel: AppViewModel = viewModel(
                    factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
                )

                LaunchedEffect(Unit) {
                    appViewModel.initGoogleSignIn(this@MainActivity)
                }

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
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

                NavGraph(
                    navController = rememberNavController(),
                    viewModel = appViewModel,
                    onGoogleSignInClick = {
                        try {
                            appViewModel.getGoogleSignInIntent()?.let { intent ->
                                launcher.launch(intent)
                            }
                        } catch (e: Exception) {
                            appViewModel.setError("Failed to start Google Sign In: ${e.message}")
                        }
                    }
                )
            }
        }
    }
}