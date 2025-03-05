package com.denizcan.substracktion

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.denizcan.substracktion.navigation.NavGraph
import com.denizcan.substracktion.ui.theme.SubstracktionTheme
import com.denizcan.substracktion.viewmodel.AppViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import androidx.lifecycle.ViewModelProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            val viewModel: AppViewModel = viewModel(
                factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            )
            
            LaunchedEffect(Unit) {
                viewModel.initGoogleSignIn(this@MainActivity)
            }

            SubstracktionTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartActivityForResult()
                    ) { result ->
                        if (result.resultCode == Activity.RESULT_OK) {
                            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                            try {
                                val account = task.getResult(ApiException::class.java)
                                account.idToken?.let { token ->
                                    viewModel.signInWithGoogle(token)
                                }
                            } catch (e: ApiException) {
                                when (e.statusCode) {
                                    GoogleSignInStatusCodes.SIGN_IN_CANCELLED -> {
                                        // Kullanıcı iptal etti, hata göstermeye gerek yok
                                    }
                                    else -> {
                                        viewModel.setError("Google sign in failed: ${e.message}")
                                    }
                                }
                            }
                        }
                    }

                    NavGraph(
                        navController = rememberNavController(),
                        viewModel = viewModel,
                        onGoogleSignInClick = {
                            try {
                                viewModel.getGoogleSignInIntent()?.let { intent ->
                                    launcher.launch(intent)
                                }
                            } catch (e: Exception) {
                                viewModel.setError("Failed to start Google Sign In: ${e.message}")
                            }
                        }
                    )
                }
            }
        }
    }
}