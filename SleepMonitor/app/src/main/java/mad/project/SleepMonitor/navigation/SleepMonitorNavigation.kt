package mad.project.SleepMonitor.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mad.project.SleepMonitor.data.network.RetrofitInstance
import mad.project.SleepMonitor.data.repository.AnalyticsRepositoryImpl

import mad.project.SleepMonitor.data.repository.ProfileRepository
import mad.project.SleepMonitor.factory.AnalyticsViewModelFactory
import mad.project.SleepMonitor.factory.ProfileViewModelFactory
import mad.project.SleepMonitor.data.repository.AuthRepositoryImpl
import mad.project.SleepMonitor.factory.AuthViewModelFactory
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
import mad.project.SleepMonitor.viewmodels.ProfileViewModel
import mad.project.SleepMonitor.viewmodels.AuthViewModel


sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash_screen")
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
    val context = LocalContext.current

    val navController = rememberNavController()
    val apiService = RetrofitInstance.analyticsApi
    val repository = AnalyticsRepositoryImpl(apiService)
    val authRepository = AuthRepositoryImpl(RetrofitInstance.AuthApi, context)

    // Profile setup
    val profileApi = RetrofitInstance.profileApi
    val profileRepository = ProfileRepository(profileApi)

    NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {

        composable(Screen.LoginScreen.route) {
            val factory = AuthViewModelFactory(authRepository)
            val authViewModel: AuthViewModel = viewModel(factory = factory)

            LoginScreen(navController = navController, viewModel = authViewModel)
        }
        composable(Screen.SignUpScreen.route) {
            val factory = AuthViewModelFactory(authRepository)
            val authViewModel: AuthViewModel = viewModel(factory = factory)

            SignUpScreen(navController = navController, viewModel = authViewModel)
        }
        composable(Screen.SplashScreen.route) {
            SplashScreen(navController)
        }

        composable(Screen.MainScreen.route) {
            MainScreen(navController)
        }
        composable(Screen.ProfileScreen.route) {
            val profileFactory = ProfileViewModelFactory(profileRepository)
            val profileViewModel: ProfileViewModel = viewModel(factory = profileFactory)
            ProfileScreen(navController = navController, viewModel = profileViewModel)
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
