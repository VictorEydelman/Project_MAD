package mad.project.SleepMonitor

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import mad.project.SleepMonitor.app.SleepMonitorApp
import mad.project.SleepMonitor.notification.NotificationService
import java.util.*

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val NotificationService = NotificationService(this)

        setContent {
            SleepMonitorApp()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                RequestNotificationPermission {
                    scheduleNotification(NotificationService)
                }
            } else {
                scheduleNotification(NotificationService)
            }
        }
    }
    // планирования уведомления на определённое время
    private fun scheduleNotification(NotificationService: NotificationService) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 13)
            set(Calendar.MINUTE, 37)
            set(Calendar.SECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }
        NotificationService.scheduleNotification(calendar.timeInMillis)
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
