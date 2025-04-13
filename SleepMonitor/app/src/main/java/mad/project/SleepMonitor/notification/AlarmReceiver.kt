package mad.project.SleepMonitor.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

// Приемник для обработки событий
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationService = NotificationService(context)
        notificationService.showNotification()
    }
}