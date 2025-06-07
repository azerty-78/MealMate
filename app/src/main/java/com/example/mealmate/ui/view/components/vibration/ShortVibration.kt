package com.example.mealmate.ui.view.components.vibration

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ShortVibration(
    context: Context,
) {
    val vibrator = context.getSystemService(Vibrator::class.java)

    //verifions si la vibration est disponible sur le device
    if(vibrator.hasVibrator()){
        // Créer un VibrationEffect selon la version d'Android
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        }else{
            vibrator.vibrate(50)
        }
    }
}