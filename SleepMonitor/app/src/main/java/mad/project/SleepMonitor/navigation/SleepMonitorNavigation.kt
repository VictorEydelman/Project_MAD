package mad.project.SleepMonitor.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import mad.project.SleepMonitor.screens.LoginScreen
import mad.project.SleepMonitor.screens.MainScreen
import mad.project.SleepMonitor.screens.SignUpScreen
import mad.project.SleepMonitor.screens.SplashScreen

sealed class Screen(val route: String) {
    object LoginScreen : Screen("login_screen")
    object SignUpScreen : Screen("signup_screen")
    object SplashScreen : Screen("splash_screen")
    object MainScreen : Screen("main_screen")
}

@Composable
fun SleepMonitorNavigation(navController: NavController) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
        composable(Screen.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(Screen.SignUpScreen.route) {
            SignUpScreen(navController)
        }
        composable(Screen.SplashScreen.route) {
            SplashScreen(navController)
        }
        composable(Screen.MainScreen.route) {
            MainScreen(navController)
        }
    }
}
