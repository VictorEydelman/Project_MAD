package mad.project.SleepMonitor.ui.common

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
fun BottomNavBar(navController: NavController, currentRoute: String?) {
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
                    color = Color.White
                )
            },
            selected = currentRoute == Screen.MainScreen.route,
            onClick = {
                if (currentRoute != Screen.MainScreen.route) {
                    navController.navigate(Screen.MainScreen.route) {
                        popUpTo(Screen.MainScreen.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        )

        // --- Элемент Analytics ---
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bar_chart),
                    contentDescription = "Analytics",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    "analytics",
                    fontSize = 12.sp,
                    color = Color.White
                )
            },
            selected = currentRoute == Screen.AnalyticsScreen.route,
            onClick = {
                if (currentRoute != Screen.AnalyticsScreen.route) {
                    navController.navigate(Screen.AnalyticsScreen.route) {
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
            selected = currentRoute == Screen.ProfileScreen.route,
            onClick = {
                if (currentRoute != Screen.ProfileScreen.route) {
                    navController.navigate(Screen.ProfileScreen.route) {
                        popUpTo(Screen.MainScreen.route)
                        launchSingleTop = true
                    }
                }
            }
        )
    }
}