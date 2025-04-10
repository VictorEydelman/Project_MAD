package mad.project.SleepMonitor.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import mad.project.SleepMonitor.navigation.SleepMonitorNavigation

@Composable
fun SleepMonitorApp() {
    val navController = rememberNavController()
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        SleepMonitorNavigation(navController)
    }
}

