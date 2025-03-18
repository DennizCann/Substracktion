package com.denizcan.substracktion.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.denizcan.substracktion.model.Price
import com.denizcan.substracktion.model.ServicePlan
import com.denizcan.substracktion.util.Language
import com.denizcan.substracktion.viewmodel.SubscriptionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailScreen(
    viewModel: SubscriptionViewModel = viewModel(),
    serviceId: String,
    onBack: () -> Unit,
    language: Language = Language.TURKISH
) {
    val plans = viewModel.selectedServicePlans.collectAsState().value
    val userCountry = viewModel.userCountry.collectAsState().value
    val error = viewModel.error.collectAsState().value
    
    LaunchedEffect(serviceId) {
        viewModel.loadServicePlans(serviceId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (language == Language.TURKISH) "Plan Seçimi"
                        else "Select Plan"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = if (language == Language.TURKISH) "Geri" else "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (plans.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
            ) {
                items(plans) { plan ->
                    PlanCard(
                        plan = plan,
                        price = viewModel.getPriceForCountry(plan),
                        onClick = {
                            // TODO: Plan seçimi ve üyelik oluşturma
                        }
                    )
                }
            }
        }

        // Hata mesajını göster
        error?.let {
            Toast.makeText(LocalContext.current, it, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun PlanCard(
    plan: ServicePlan,
    price: Price?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = plan.name,
                style = MaterialTheme.typography.titleLarge
            )
            
            Text(
                text = plan.description,
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            price?.let {
                Text(
                    text = "${it.amount} ${it.currency}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
} 