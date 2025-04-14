package mad.project.SleepMonitor.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mad.project.SleepMonitor.screens.SplashScreen
import mad.project.SleepMonitor.screens.LoginScreen
import mad.project.SleepMonitor.screens.SignUpScreen
import mad.project.SleepMonitor.screens.MainScreen
import mad.project.SleepMonitor.screens.ProfileScreen

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash_screen") // Экран сплеша
    object LoginScreen : Screen("login_screen")
    object SignUpScreen : Screen("signup_screen")
    object MainScreen : Screen("main_screen")
    object ProfileScreen : Screen("profile_screen")
}

@Composable



fun SleepMonitorNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {
        composable(Screen.SplashScreen.route) {
            SplashScreen(navController = navController)
        }
        composable(Screen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.SignUpScreen.route) {
            SignUpScreen(navController = navController)
        }
        composable(Screen.MainScreen.route) {
            MainScreen(navController = navController)
        }
        composable(Screen.ProfileScreen.route) {
            ProfileScreen(navController = navController)
        }
    }
}
