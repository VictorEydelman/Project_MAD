package mad.project.SleepMonitor.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.screens.LoginScreen
import com.example.myapplication.screens.SignUpScreen
import mad.project.SleepMonitor.navigation.Screen

@Composable
fun SleepMonitorApp() {
    val navController = rememberNavController()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.LoginScreen.route
        ) {
            composable(Screen.LoginScreen.route) {
                LoginScreen(navController = navController)
            }
            composable(Screen.SignUpScreen.route) {
                SignUpScreen(navController = navController)
            }
        }
    }
}
