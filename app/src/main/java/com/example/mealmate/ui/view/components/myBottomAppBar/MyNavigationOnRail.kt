package com.example.mealmate.ui.view.components.myBottomAppBar

import androidx.compose.foundation.layout.size
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlin.compareTo
import kotlin.toString

@Composable
fun MyNavigationOnRail(
    navController : NavController
) {
    NavigationRail(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        header = {
            Text("Mima'a")
        }
    ) {
        val navBackSactEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackSactEntry?.destination?.route

        val isMainRoute = bottomNavItems.any { it.route == currentRoute } // Filtre pour ne garder que les routes principales

        bottomNavItems.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationRailItem(
                selected = isSelected && isMainRoute,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.badge > 0) Badge { Text(item.badge.toString()) }
                            else if (item.hasNews) Badge()
                        }
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (isSelected) item.selectedIcon
                                else item.unselectedIcon
                            ),
                            contentDescription = item.title,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = { Text(item.title) },
                alwaysShowLabel = false,
            )
        }

    }

}