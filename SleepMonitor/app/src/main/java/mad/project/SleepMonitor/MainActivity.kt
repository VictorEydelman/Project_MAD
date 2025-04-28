package mad.project.SleepMonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import mad.project.SleepMonitor.app.SleepMonitorApp
import mad.project.SleepMonitor.notification.NotificationService


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val NotificationService = NotificationService(applicationContext)

        setContent {
            SleepMonitorApp(NotificationService)
        }
    }
}
//  Используем экспериментальное API для управления разрешениями
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestNotificationPermission(onPermissionGranted: () -> Unit) {
    val permissionState = rememberPermissionState(
        permission = android.Manifest.permission.POST_NOTIFICATIONS // Разрешение на уведомления
    )
    LaunchedEffect(permissionState.status) {
        if (permissionState.status.isGranted) {
            onPermissionGranted()
        } else {
            permissionState.launchPermissionRequest()
        }
    }
}
