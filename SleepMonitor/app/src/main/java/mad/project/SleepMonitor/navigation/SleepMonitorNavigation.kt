package mad.project.SleepMonitor.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mad.project.SleepMonitor.data.network.RetrofitInstance
import mad.project.SleepMonitor.data.repository.AnalyticsRepositoryImpl
import mad.project.SleepMonitor.factory.AnalyticsViewModelFactory
import mad.project.SleepMonitor.notification.NotificationService
import mad.project.SleepMonitor.screens.AlarmScreen
import mad.project.SleepMonitor.screens.AnalyticsScreen
import mad.project.SleepMonitor.screens.BedTimeScreen
import mad.project.SleepMonitor.screens.SplashScreen
import mad.project.SleepMonitor.screens.LoginScreen
import mad.project.SleepMonitor.screens.SignUpScreen
import mad.project.SleepMonitor.screens.MainScreen
import mad.project.SleepMonitor.screens.ProfileScreen
import mad.project.SleepMonitor.viewmodels.AnalyticsViewModel

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash_screen") // Экран сплеша
    object LoginScreen : Screen("login_screen")
    object SignUpScreen : Screen("signup_screen")
    object MainScreen : Screen("main_screen")
    object ProfileScreen : Screen("profile_screen")
    object AnalyticsScreen : Screen("analytics_screen")
    object AlarmScreen : Screen("alarm")
    object BedTimeScreen : Screen("bedtime")
}

@Composable
fun SleepMonitorNavigation(notification: NotificationService) {
    val navController = rememberNavController()
    val apiService = RetrofitInstance.analyticsApi
    val repository = AnalyticsRepositoryImpl(apiService)

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
        composable(Screen.ProfileScreen.route) {
            ProfileScreen(navController)
        }
        composable(Screen.AlarmScreen.route) {
            AlarmScreen(navController, notification)
        }
        composable(Screen.BedTimeScreen.route) {
            BedTimeScreen(navController, notification)
        }
        composable(Screen.AnalyticsScreen.route) {
            val factory = AnalyticsViewModelFactory(repository)
            val analyticsViewModel: AnalyticsViewModel = viewModel(factory = factory)

            AnalyticsScreen(navController = navController, viewModel = analyticsViewModel)
        }
    }
}
