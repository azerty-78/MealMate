package com.example.mealmate.ui.view.authentification


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun FullScreenLoader(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Black.copy(alpha = 0.5f),
    progressIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    progressIndicatorSize: Dp = 64.dp,
    strokeWidth: Dp = 6.dp,
    loadingText: String? = null
) {
    if (isVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .then(modifier),
            contentAlignment = Alignment.Center
        ) {
            // Conteneur du loader avec fond légèrement visible
            Surface(
                modifier = Modifier
                    .width(200.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Indicateur circulaire avec animation
                    CircularProgressIndicator(
                        modifier = Modifier.size(progressIndicatorSize),
                        color = progressIndicatorColor,
                        strokeWidth = strokeWidth,
                        strokeCap = StrokeCap.Round
                    )

                    // Texte optionnel
                    loadingText?.let {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}