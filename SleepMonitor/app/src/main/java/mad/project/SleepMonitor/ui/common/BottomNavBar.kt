package mad.project.SleepMonitor.ui.common // Убедись, что пакет правильный

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import mad.project.SleepMonitor.R
import mad.project.SleepMonitor.navigation.Screen

@Composable
fun BottomNavBar(navController: NavController, currentRoute: String?) { // Параметры верные: NavController и текущий маршрут
    NavigationBar(
        containerColor = Color(0xFF152238),
        tonalElevation = 8.dp
    ) {
        // --- Элемент Home ---
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    "home",
                    fontSize = 12.sp,
                    color = Color.White // Можно вынести цвета в константы или тему
                )
            },
            // Выбран, если текущий маршрут совпадает с маршрутом MainScreen
            selected = currentRoute == Screen.MainScreen.route,
            onClick = {
                // Навигация только если мы НЕ на этом экране
                if (currentRoute != Screen.MainScreen.route) {
                    navController.navigate(Screen.MainScreen.route) {
                        // Очистка стека до MainScreen и singleTop для стандартного поведения табов
                        popUpTo(Screen.MainScreen.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        )

        // --- Элемент Abilities ---
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bar_chart),
                    contentDescription = "Abilities",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    "abilities",
                    fontSize = 12.sp,
                    color = Color.White
                )
            },
            // Выбран, если текущий маршрут совпадает с маршрутом AbilitiesScreen
            selected = currentRoute == Screen.AbilitiesScreen.route,
            onClick = {
                // Навигация только если мы НЕ на этом экране
                if (currentRoute != Screen.AbilitiesScreen.route) {
                    navController.navigate(Screen.AbilitiesScreen.route) {
                        // Возврат к MainScreen (корневому экрану табов) и singleTop
                        popUpTo(Screen.MainScreen.route)
                        launchSingleTop = true
                    }
                }
            }
        )

        // --- Элемент Profile ---
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    "profile",
                    fontSize = 12.sp,
                    color = Color.White
                )
            },
            // Выбран, если текущий маршрут совпадает с маршрутом ProfileScreen
            selected = currentRoute == Screen.ProfileScreen.route,
            onClick = {
                // Навигация только если мы НЕ на этом экране
                if (currentRoute != Screen.ProfileScreen.route) {
                    navController.navigate(Screen.ProfileScreen.route) {
                        // Возврат к MainScreen (корневому экрану табов) и singleTop
                        popUpTo(Screen.MainScreen.route)
                        launchSingleTop = true
                    }
                }
            }
        )
    }
}