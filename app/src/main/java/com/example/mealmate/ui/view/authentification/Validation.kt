package com.example.mealmate.ui.view.authentification

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.example.mealmate.R

fun isValidEmail(email: String): Boolean {
    if (email.isEmpty() || email.length > 254) return false
    if (email.count { it == '@' } != 1) return false

    val parts = email.split("@")
    if (parts.size != 2) return false

    val localPart = parts[0]
    val domainPart = parts[1]

    // Validation partie locale (avant @)
    val localRegex = Regex("^[A-Za-z0-9](?:[A-Za-z0-9._%+-]*[A-Za-z0-9])?\$")
    if (!localPart.matches(localRegex) || localPart.startsWith('.') || localPart.endsWith('.')) {
        return false
    }

    // Validation domaine
    if (domainPart.startsWith('.') || domainPart.endsWith('.') || domainPart.contains("..")) {
        return false
    }

    val domainRegex = Regex("^[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
    return domainPart.matches(domainRegex)
}


fun validatePassword(password: String): ValidationResult {
    val requirements = listOf(
        Requirement("8 caractères minimum", password.length >= 8),
        Requirement("1 majuscule (A-Z)", password.any { it.isUpperCase() }),
        Requirement("1 minuscule (a-z)", password.any { it.isLowerCase() }),
        Requirement("1 chiffre (0-9)", password.any { it.isDigit() }),
        Requirement("1 caractère spécial", password.any { !it.isLetterOrDigit() }),
        Requirement("Pas de mot courant", isNotCommonPassword(password)),
        Requirement("Pas de répétitions", hasNoRepeats(password)),
        Requirement("Pas d'espaces", !password.contains(" "))
    )

    return ValidationResult(
        isValid = requirements.all { it.isMet },
        requirements = requirements
    )
}

data class ValidationResult(
    val isValid: Boolean,
    val requirements: List<Requirement>
)

data class Requirement(
    val description: String,
    val isMet: Boolean
)


// Vérifie les mots de passe courants
fun isNotCommonPassword(password: String): Boolean {
    val commonPasswords = setOf("userPassword", "123456", "qwerty", "azerty", "000000")
    return !commonPasswords.contains(password.lowercase())
}

// Vérifie les répétitions
fun hasNoRepeats(password: String): Boolean {
    return !Regex("(.)\\1{2,}").containsMatchIn(password)
}


@Composable
fun AnimatedPasswordRequirements(
    password: String,
    showRequirements: Boolean,
    modifier: Modifier = Modifier
) {
    val requirements = remember(password) { getPasswordRequirements(password) }
    val transition = updateTransition(showRequirements, label = "requirementsTransition")

    val alpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 200) },
        label = "alpha"
    ) { if (it) 1f else 0f }

    val height by transition.animateDp(
        transitionSpec = { tween(durationMillis = 200) },
        label = "height"
    ) { if (it) 150.dp else 0.dp }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .alpha(alpha)
    ) {
        if (showRequirements) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    "Votre mot de passe doit contenir :",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                requirements.forEach { req ->
                    RequirementItem(
                        description = req.description,
                        isMet = req.isMet
                    )
                }
            }
        }
    }
}

fun getPasswordRequirements(password: String): List<Requirement> {
    return listOf(
        Requirement("• 8 caractères minimum", password.length >= 8),
        Requirement("• 1 majuscule et 1 minuscule",
            password.any { it.isUpperCase() } && password.any { it.isLowerCase() }),
        Requirement("• 1 chiffre (0-9)", password.any { it.isDigit() }),
        Requirement("• 1 caractère spécial", password.any { !it.isLetterOrDigit() }),
        Requirement("• Pas de mot courant", isNotCommonPassword(password))
    )
}

@Composable
fun RequirementItem(description: String, isMet: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Icon(
            imageVector = if (isMet) Icons.Default.CheckCircle else Icons.Default.Info,
            contentDescription = null,
            tint = if (isMet) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.labelSmall,
            color = if (isMet) MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
