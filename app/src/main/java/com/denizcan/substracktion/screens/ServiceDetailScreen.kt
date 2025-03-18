package com.denizcan.substracktion.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.denizcan.substracktion.model.Price
import com.denizcan.substracktion.model.ServicePlan
import com.denizcan.substracktion.viewmodel.SubscriptionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailScreen(
    viewModel: SubscriptionViewModel = viewModel(),
    serviceId: String,
    onBack: () -> Unit
) {
    val plans by viewModel.selectedServicePlans.collectAsState()
    
    LaunchedEffect(serviceId) {
        viewModel.loadServicePlans(serviceId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Plan Seçimi") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
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