package mad.project.SleepMonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mad.project.SleepMonitor.navigation.SleepMonitorNavigation
import mad.project.SleepMonitor.screens.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SleepMonitorNavigation()
        }
    }
}

@Composable
fun SleepMonitorApp() {
    val navController = rememberNavController() // создаём NavController

    // Оборачиваем всё в NavHost
    NavHost(
        navController = navController,
        startDestination = "main" // указываем стартовый экран
    ) {
        // Регистрируем экраны
        composable("main") {
            MainScreen(navController) // Привязываем экраны к навигации
        }
        // Можно добавить другие экраны, например:
        // composable("profile") { ProfileScreen(navController) }
    }
}
