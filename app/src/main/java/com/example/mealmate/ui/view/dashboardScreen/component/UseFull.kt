package com.example.mealmate.ui.view.dashboardScreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import com.example.mealmate.R

data class GlycemiaReading(
    val time: LocalTime,
    val value: Int, // mg/dL
    val type: String,
    val status: GlycemiaStatus
)

enum class GlycemiaStatus(val color: Color, val label: String) {
    HYPOGLYCEMIA(Color(0xFFE53E3E), "Hypoglycémie"),
    NORMAL(Color(0xFF38A169), "Normal"),
    HYPERGLYCEMIA(Color(0xFFDD6B20), "Hyperglycémie")
}

data class CameroonianDish(
    val name: String,
    val category: String,
    val glucides: Int, // grammes
    val calories: Int,
    val fibres: Int,
    val indexGlycemique: Int,
    val portionRecommandee: String,
    val ingredients: List<String>,
    val preparationTips: String
)

data class MealEntry(
    val time: LocalTime,
    val dish: CameroonianDish,
    val portion: Double, // multiplicateur de la portion recommandée
    val glycemiaAfter: Int? = null,
    val notes: String = ""
)

data class WeeklyTrend(
    val day: String,
    val matin: Int,
    val midi: Int,
    val soir: Int,
    val moyenne: Int
)

data class NutritionSummary(
    val totalGlucides: Int,
    val totalCalories: Int,
    val totalFibres: Int,
    val moyenneIG: Int
)

data class MedicationReminder(
    val name: String,
    val dose: String,
    val time: LocalTime,
    val taken: Boolean
)

data class HealthAlert(
    val type: AlertType,
    val message: String,
    val time: LocalTime
)

enum class AlertType(val color: Color, val icon: String) {
    WARNING(Color(0xFFED8936), "⚠️"),
    INFO(Color(0xFF3182CE), "ℹ️"),
    SUCCESS(Color(0xFF38A169), "✅")
}

// Données de plats camerounais adaptés aux diabétiques
object CameroonianDiabetesData {
    val dishes = listOf(
        CameroonianDish(
            name = "Ndolé aux légumes",
            category = "Plat principal",
            glucides = 25,
            calories = 320,
            fibres = 8,
            indexGlycemique = 35,
            portionRecommandee = "150g",
            ingredients = listOf("Feuilles de ndolé", "Poisson fumé", "Arachides", "Légumes verts"),
            preparationTips = "Réduire les arachides, augmenter les légumes verts"
        ),
        CameroonianDish(
            name = "Riz au poisson braisé",
            category = "Plat principal",
            glucides = 45,
            calories = 380,
            fibres = 3,
            indexGlycemique = 50,
            portionRecommandee = "100g riz + 120g poisson",
            ingredients = listOf("Riz étuvé", "Poisson frais", "Tomates", "Oignons", "Épices"),
            preparationTips = "Préférer le riz étuvé, portion réduite"
        ),
        CameroonianDish(
            name = "Salade de haricots verts",
            category = "Accompagnement",
            glucides = 12,
            calories = 85,
            fibres = 6,
            indexGlycemique = 25,
            portionRecommandee = "200g",
            ingredients = listOf("Haricots verts", "Tomates", "Oignons", "Huile d'olive"),
            preparationTips = "Excellent choix pour les diabétiques"
        ),
        CameroonianDish(
            name = "Plantain bouilli",
            category = "Féculent",
            glucides = 35,
            calories = 150,
            fibres = 4,
            indexGlycemique = 55,
            portionRecommandee = "1 petit plantain",
            ingredients = listOf("Plantain vert", "Sel"),
            preparationTips = "Préférer le plantain vert, portion modérée"
        ),
        CameroonianDish(
            name = "Poisson à la sauce tomate",
            category = "Plat principal",
            glucides = 8,
            calories = 250,
            fibres = 3,
            indexGlycemique = 20,
            portionRecommandee = "150g",
            ingredients = listOf("Poisson frais", "Tomates fraîches", "Oignons", "Épices"),
            preparationTips = "Excellente source de protéines"
        ),
        CameroonianDish(
            name = "Légumes sautés (gombo)",
            category = "Accompagnement",
            glucides = 15,
            calories = 95,
            fibres = 7,
            indexGlycemique = 15,
            portionRecommandee = "200g",
            ingredients = listOf("Gombo", "Tomates", "Oignons", "Épices locales"),
            preparationTips = "Très bon pour contrôler la glycémie"
        ),
        CameroonianDish(
            name = "Eru aux légumes",
            category = "Plat principal",
            glucides = 20,
            calories = 280,
            fibres = 9,
            indexGlycemique = 30,
            portionRecommandee = "180g",
            ingredients = listOf("Feuilles d'eru", "Poisson séché", "Légumes verts"),
            preparationTips = "Riche en fibres, excellent pour les diabétiques"
        ),
        CameroonianDish(
            name = "Banane plantain grillée",
            category = "Collation",
            glucides = 30,
            calories = 130,
            fibres = 5,
            indexGlycemique = 45,
            portionRecommandee = "1/2 plantain moyen",
            ingredients = listOf("Plantain mûr"),
            preparationTips = "Griller sans huile, portion contrôlée"
        )
    )

    val sampleGlycemiaData = listOf(
        GlycemiaReading(LocalTime.of(6, 30), 95, "Jeûne", GlycemiaStatus.NORMAL),
        GlycemiaReading(LocalTime.of(8, 30), 135, "Post-repas", GlycemiaStatus.NORMAL),
        GlycemiaReading(LocalTime.of(12, 0), 110, "Pré-repas", GlycemiaStatus.NORMAL),
        GlycemiaReading(LocalTime.of(14, 0), 155, "Post-repas", GlycemiaStatus.HYPERGLYCEMIA),
        GlycemiaReading(LocalTime.of(18, 0), 105, "Pré-repas", GlycemiaStatus.NORMAL),
        GlycemiaReading(LocalTime.of(20, 30), 145, "Post-repas", GlycemiaStatus.NORMAL),
        GlycemiaReading(LocalTime.of(22, 0), 118, "Coucher", GlycemiaStatus.NORMAL)
    )

    val sampleMeals = listOf(
        MealEntry(
            LocalTime.of(7, 30),
            dishes[6], // Eru aux légumes
            1.0,
            135,
            "Bon petit-déjeuner riche en fibres"
        ),
        MealEntry(
            LocalTime.of(12, 30),
            dishes[0], // Ndolé aux légumes
            1.0,
            155,
            "Portion normale, surveiller la glycémie"
        ),
        MealEntry(
            LocalTime.of(19, 0),
            dishes[4], // Poisson à la sauce tomate
            1.0,
            145,
            "Excellent choix pour le dîner"
        )
    )

    val weeklyTrends = listOf(
        WeeklyTrend("Lun", 98, 135, 118, 117),
        WeeklyTrend("Mar", 102, 142, 125, 123),
        WeeklyTrend("Mer", 95, 138, 115, 116),
        WeeklyTrend("Jeu", 105, 155, 130, 130),
        WeeklyTrend("Ven", 100, 145, 122, 122),
        WeeklyTrend("Sam", 108, 160, 135, 134),
        WeeklyTrend("Dim", 97, 140, 120, 119)
    )

    val medications = listOf(
        MedicationReminder("Metformine", "500mg", LocalTime.of(8, 0), true),
        MedicationReminder("Insuline rapide", "8 UI", LocalTime.of(12, 0), true),
        MedicationReminder("Insuline lente", "20 UI", LocalTime.of(22, 0), false)
    )

    val alerts = listOf(
        HealthAlert(AlertType.WARNING, "Glycémie élevée après le déjeuner", LocalTime.of(14, 15)),
        HealthAlert(AlertType.INFO, "Rappel: Prise de médicament dans 30min", LocalTime.of(21, 30)),
        HealthAlert(AlertType.SUCCESS, "Objectif glycémique atteint ce matin", LocalTime.of(8, 30))
    )
}


@Composable
fun HeaderSection(
    selectedPeriod: String,
    onPeriodSelected: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            //containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Tableau de Bord Diabète",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A202C)
            )
            Text(
                text = "Suivi personnalisé avec plats camerounais",
                fontSize = 14.sp,
                color = Color(0xFF718096),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listOf("Aujourd'hui", "7 jours", "30 jours")) { period ->
                    FilterChip(
                        onClick = { onPeriodSelected(period) },
                        label = { Text(period) },
                        selected = selectedPeriod == period,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF3182CE),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun QuickStatsSection(
    lastGlycemia: GlycemiaReading,
    onGlycemiaClick: () -> Unit,
    onNutritionClick: () -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            StatCard(
                title = "Glycémie Actuelle",
                value = "${lastGlycemia.value}",
                unit = "mg/dL",
                icon = Icons.Default.Favorite,
                color = lastGlycemia.status.color,
                trend = "-2.5%",
                onClick = onGlycemiaClick
            )
        }
        item {
            StatCard(
                title = "HbA1c",
                value = "6.8",
                unit = "%",
                icon = Icons.Default.Info,
                color = Color(0xFF9F7AEA),
                trend = "-0.3%",
                onClick = {}
            )
        }
        item {
            StatCard(
                title = "Moyenne 7j",
                value = "118",
                unit = "mg/dL",
                icon = Icons.Default.DateRange,
                color = Color(0xFF4299E1),
                trend = "+1.2%",
                onClick = {}
            )
        }
        item {
            StatCard(
                title = "Objectifs",
                value = "78",
                unit = "%",
                icon = Icons.Default.Face,
                color = Color(0xFF48BB78),
                trend = "+5%",
                onClick = onNutritionClick
            )
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    unit: String,
    icon: ImageVector,
    color: Color,
    trend: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = title,
                        fontSize = 12.sp,
                        color = Color(0xFF718096)
                    )
                    Text(
                        text = "$value $unit",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A202C)
                    )
                    Text(
                        text = trend,
                        fontSize = 10.sp,
                        color = if (trend.startsWith("+")) Color(0xFFE53E3E) else Color(0xFF38A169)
                    )
                }
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun GlycemiaChartCard(
    data: List<GlycemiaReading>,
    onDetailsClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            //containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Évolution Glycémie",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = onDetailsClick) {
                    Text("Détails")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Graphique simple avec barres
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(data) { reading ->
                    GlycemiaBar(reading = reading)
                }
            }
        }
    }
}

@Composable
fun GlycemiaBar(reading: GlycemiaReading) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(60.dp)
    ) {
        Box(
            modifier = Modifier
                .width(12.dp)
                .height((reading.value / 2).dp)
                .background(
                    reading.status.color,
                    RoundedCornerShape(6.dp)
                )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${reading.value}",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = reading.status.color
        )
        Text(
            text = reading.time.format(DateTimeFormatter.ofPattern("HH:mm")),
            fontSize = 8.sp,
            color = Color(0xFF718096)
        )
    }
}

@Composable
fun MealsSection(
    meals: List<MealEntry>,
    onMealClick: (MealEntry) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            //containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Repas du Jour",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            meals.forEach { meal ->
                MealCard(
                    meal = meal,
                    onClick = { onMealClick(meal) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun MealCard(
    meal: MealEntry,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF7FAFC)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        getIGColor(meal.dish.indexGlycemique),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = meal.dish.indexGlycemique.toString(),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = meal.dish.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = "${meal.time.format(DateTimeFormatter.ofPattern("HH:mm"))} • ${meal.dish.calories} kcal",
                    fontSize = 12.sp,
                    color = Color(0xFF718096)
                )
            }

            meal.glycemiaAfter?.let { glycemia ->
                Box(
                    modifier = Modifier
                        .padding(start = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${glycemia} mg/dL",
                        fontSize = 12.sp,
                        color = getGlycemiaColor(glycemia),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun WeeklyTrendsCard(trends: List<WeeklyTrend>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            //containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Tendances Hebdomadaires",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(trends) { trend ->
                    WeeklyTrendItem(trend = trend)
                }
            }
        }
    }
}

@Composable
fun WeeklyTrendItem(trend: WeeklyTrend) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Text(
            text = trend.day,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4A5568)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Barres pour matin, midi, soir
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            TrendBar(value = trend.matin, maxValue = 180, color = Color(0xFF4299E1))
            TrendBar(value = trend.midi, maxValue = 180, color = Color(0xFF48BB78))
            TrendBar(value = trend.soir, maxValue = 180, color = Color(0xFF9F7AEA))
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "${trend.moyenne}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = getGlycemiaColor(trend.moyenne)
        )
    }
}

@Composable
fun TrendBar(value: Int, maxValue: Int, color: Color) {
    Box(
        modifier = Modifier
            .width(60.dp)
            .height(4.dp)
            .background(Color(0xFFE2E8F0), RoundedCornerShape(2.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth((value.toFloat() / maxValue))
                .background(color, RoundedCornerShape(2.dp))
        )
    }
}

@Composable
fun MedicationsCard(
    medications: List<MedicationReminder>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            //containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Médicaments",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            medications.forEach { med ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (med.taken) Icons.Default.CheckCircle else Icons.Default.DateRange,
                        contentDescription = null,
                        tint = if (med.taken) Color(0xFF38A169) else Color(0xFFED8936),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = med.name,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${med.dose} • ${med.time.format(DateTimeFormatter.ofPattern("HH:mm"))}",
                            fontSize = 10.sp,
                            color = Color(0xFF718096)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AlertsCard(
    alerts: List<HealthAlert>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            //containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Alertes",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            alerts.forEach { alert ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = alert.type.color.copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = alert.type.icon,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = alert.message,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = alert.time.format(DateTimeFormatter.ofPattern("HH:mm")),
                                fontSize = 9.sp,
                                color = Color(0xFF718096)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun NutritionRecommendationsCard(dishes: List<CameroonianDish>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            //containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Plats Recommandés (Cuisine Camerounaise)",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(dishes.filter { it.indexGlycemique <= 40 }) { dish ->
                    RecommendedDishCard(dish = dish)
                }
            }
        }
    }
}

@Composable
fun RecommendedDishCard(dish: CameroonianDish) {
    Card(
        modifier = Modifier.width(200.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0FFF4),
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = dish.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            getIGColor(dish.indexGlycemique),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "IG",
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = dish.category,
                fontSize = 12.sp,
                color = Color(0xFF718096)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${dish.glucides}g glucides",
                    fontSize = 10.sp,
                    color = Color(0xFF4A5568)
                )
                Text(
                    text = "${dish.calories} kcal",
                    fontSize = 10.sp,
                    color = Color(0xFF4A5568)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Portion: ${dish.portionRecommandee}",
                fontSize = 10.sp,
                color = Color(0xFF38A169),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Dialogs
@Composable
fun GlycemiaDetailsDialog(
    data: List<GlycemiaReading>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Détails Glycémie",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Fermer")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.heightIn(max = 400.dp)
                ) {
                    items(data) { reading ->
                        GlycemiaDetailItem(reading = reading)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Statistiques
                val average = data.map { it.value }.average().toInt()
                val min = data.minOfOrNull { it.value } ?: 0
                val max = data.maxOfOrNull { it.value } ?: 0

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatisticItem("Moyenne", "$average mg/dL")
                    StatisticItem("Min", "$min mg/dL")
                    StatisticItem("Max", "$max mg/dL")
                }
            }
        }
    }
}

@Composable
fun GlycemiaDetailItem(reading: GlycemiaReading) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = reading.status.color.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(reading.status.color, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${reading.value}",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reading.time.format(DateTimeFormatter.ofPattern("HH:mm")),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = reading.type,
                    fontSize = 12.sp,
                    color = Color(0xFF718096)
                )
            }

            Text(
                text = reading.status.label,
                fontSize = 12.sp,
                color = reading.status.color,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun MealDetailsDialog(
    meal: MealEntry,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = meal.dish.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Fermer")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Informations principales
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    InfoItem("Heure", meal.time.format(DateTimeFormatter.ofPattern("HH:mm")))
                    InfoItem("Catégorie", meal.dish.category)
                    InfoItem("Portion", meal.dish.portionRecommandee)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Valeurs nutritionnelles
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF7FAFC))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Valeurs Nutritionnelles",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            NutritionItem("Glucides", "${meal.dish.glucides}g")
                            NutritionItem("Calories", "${meal.dish.calories}")
                            NutritionItem("Fibres", "${meal.dish.fibres}g")
                            NutritionItem("IG", "${meal.dish.indexGlycemique}")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Ingrédients
                Text(
                    text = "Ingrédients",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier.heightIn(max = 100.dp)
                ) {
                    items(meal.dish.ingredients) { ingredient ->
                        Row(
                            modifier = Modifier.padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .background(Color(0xFF4299E1), CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = ingredient,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Conseils
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FFF4))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "Conseils Diabétique",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF38A169)
                        )
                        Text(
                            text = meal.dish.preparationTips,
                            fontSize = 12.sp,
                            color = Color(0xFF2D3748),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                // Glycémie après repas
                meal.glycemiaAfter?.let { glycemia ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = getGlycemiaColor(glycemia).copy(alpha = 0.1f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null,
                                tint = getGlycemiaColor(glycemia)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Glycémie après repas: $glycemia mg/dL",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = getGlycemiaColor(glycemia)
                            )
                        }
                    }
                }

                if (meal.notes.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Notes: ${meal.notes}",
                        fontSize = 12.sp,
                        color = Color(0xFF718096),
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun NutritionSummaryDialog(
    meals: List<MealEntry>,
    onDismiss: () -> Unit
) {
    val totalGlucides = meals.sumOf { it.dish.glucides }
    val totalCalories = meals.sumOf { it.dish.calories }
    val totalFibres = meals.sumOf { it.dish.fibres }
    val moyenneIG = meals.map { it.dish.indexGlycemique }.average().toInt()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Résumé Nutritionnel",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Fermer")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Graphique en barres des totaux
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF7FAFC))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Apports du Jour",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        NutritionProgressBar(
                            label = "Glucides",
                            current = totalGlucides,
                            target = 150,
                            unit = "g",
                            color = Color(0xFF4299E1)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        NutritionProgressBar(
                            label = "Calories",
                            current = totalCalories,
                            target = 1800,
                            unit = "kcal",
                            color = Color(0xFF48BB78)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        NutritionProgressBar(
                            label = "Fibres",
                            current = totalFibres,
                            target = 30,
                            unit = "g",
                            color = Color(0xFF9F7AEA)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Répartition par repas
                Text(
                    text = "Répartition par Repas",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier.heightIn(max = 200.dp)
                ) {
                    items(meals) { meal ->
                        MealSummaryItem(meal = meal)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Conseils
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (moyenneIG <= 40) Color(0xFFF0FFF4) else Color(0xFFFFF5F5)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "Évaluation",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (moyenneIG <= 40) Color(0xFF38A169) else Color(0xFFE53E3E)
                        )
                        Text(
                            text = "Index glycémique moyen: $moyenneIG",
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Text(
                            text = if (moyenneIG <= 40)
                                "Excellente journée! Vos choix alimentaires sont parfaits pour votre diabète."
                            else
                                "Attention à l'index glycémique. Privilégiez les légumes verts et les protéines.",
                            fontSize = 12.sp,
                            color = Color(0xFF2D3748),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NutritionProgressBar(
    label: String,
    current: Int,
    target: Int,
    unit: String,
    color: Color
) {
    val progress = (current.toFloat() / target).coerceAtMost(1f)

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "$current/$target $unit",
                fontSize = 12.sp,
                color = Color(0xFF718096)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(Color(0xFFE2E8F0), RoundedCornerShape(4.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .background(color, RoundedCornerShape(4.dp))
            )
        }
    }
}

@Composable
fun MealSummaryItem(meal: MealEntry) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = meal.dish.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = meal.time.format(DateTimeFormatter.ofPattern("HH:mm")),
                fontSize = 10.sp,
                color = Color(0xFF718096)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "${meal.dish.glucides}g",
                fontSize = 10.sp,
                color = Color(0xFF4299E1)
            )
            Text(
                text = "${meal.dish.calories}kcal",
                fontSize = 10.sp,
                color = Color(0xFF48BB78)
            )
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(
                        getIGColor(meal.dish.indexGlycemique),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${meal.dish.indexGlycemique}",
                    color = Color.White,
                    fontSize = 6.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Composables utilitaires
@Composable
fun InfoItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF718096)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun NutritionItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2D3748)
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF718096)
        )
    }
}

@Composable
fun StatisticItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2D3748)
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF718096)
        )
    }
}

// Fonctions utilitaires
fun getGlycemiaColor(value: Int): Color {
    return when {
        value < 70 -> Color(0xFFE53E3E)
        value > 140 -> Color(0xFFDD6B20)
        else -> Color(0xFF38A169)
    }
}

fun getIGColor(ig: Int): Color {
    return when {
        ig <= 35 -> Color(0xFF38A169) // Vert - IG bas
        ig <= 50 -> Color(0xFFDD6B20) // Orange - IG modéré
        else -> Color(0xFFE53E3E) // Rouge - IG élevé
    }
}