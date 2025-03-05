package com.denizcan.substracktion.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalConfiguration
import com.denizcan.substracktion.util.Language
import com.denizcan.substracktion.util.UiText

@Composable
fun WelcomeScreen(
    onNavigateToNext: () -> Unit,
    language: Language
) {
    val text = remember(language) { UiText.getWelcomeText(language) }
    var showContent by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        showContent = true
    }

    AnimatedVisibility(
        visible = showContent,
        enter = fadeIn(animationSpec = tween(500))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Başlık Bölümü
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 48.dp)
            ) {
                Text(
                    text = "SUBSTRACKTION",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = with(LocalDensity.current) {
                            minOf(
                                MaterialTheme.typography.headlineLarge.fontSize.toPx(),
                                LocalConfiguration.current.screenWidthDp.dp.toPx() * 0.08f
                            ).toSp()
                        }
                    ),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = text.slogan,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Özellikler Bölümü - Ortalanmış
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(
                        with(LocalDensity.current) {
                            minOf(
                                28.dp,
                                (LocalConfiguration.current.screenHeightDp.dp * 0.04f)
                            )
                        }
                    )
                ) {
                    text.features.forEach { feature ->
                        FeatureItem(
                            title = feature.title,
                            description = feature.description
                        )
                    }
                }
            }

            // Buton Bölümü
            Button(
                onClick = onNavigateToNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
                    .heightIn(min = 48.dp, max = 58.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = text.getStarted,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Composable
private fun FeatureItem(
    title: String,
    description: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = with(LocalDensity.current) {
                    minOf(
                        MaterialTheme.typography.titleMedium.fontSize.toPx() * 1.2f,
                        LocalConfiguration.current.screenWidthDp.dp.toPx() * 0.045f
                    ).toSp()
                }
            ),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(6.dp))
        
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
} 