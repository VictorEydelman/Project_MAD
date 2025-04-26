package mad.project.SleepMonitor.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import mad.project.SleepMonitor.components.NormalTextComponent
import mad.project.SleepMonitor.navigation.SleepMonitorNavigation
import mad.project.SleepMonitor.notification.NotificationService

@Composable
fun SleepMonitorApp(notificationService: NotificationService) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        SleepMonitorNavigation(notificationService)
    }
}