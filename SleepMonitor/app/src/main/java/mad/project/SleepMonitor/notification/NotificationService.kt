package mad.project.SleepMonitor.notification

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import mad.project.SleepMonitor.R
import mad.project.SleepMonitor.MainActivity
import mad.project.SleepMonitor.notification.NotificationConstants.NotificationKeys.NOTIFICATION_CHANNEL_ID
import mad.project.SleepMonitor.notification.NotificationConstants.NotificationKeys.NOTIFICATION_ID
import mad.project.SleepMonitor.notification.NotificationConstants.NotificationKeys.REQUEST_CODE


class NotificationService(private val context: Context)
{
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager // менеджер уведомлений
    private val myIntent = Intent(context, MainActivity::class.java) // перехода в MainActivity при нажатии
    private val pendingIntent = PendingIntent.getActivity(
        context,
        REQUEST_CODE,
        myIntent,
        PendingIntent.FLAG_IMMUTABLE
    )

    // Сразу появление уведомлении
    fun showNotification() {
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.sleep_line)
            .setContentTitle("Twinkle, twinkle, little star")
            .setContentText(
                "Well, you had a great day, do not let the night ruin it!\n" +
                        "Good night, sleep tight!"
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    // уведомление при точном времение
    @SuppressLint("UnspecifiedImmutableFlag")
    fun scheduleNotification(timeInMillis: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // Intent, который будет отправлен при срабатывании будильника
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        // Устанавливать точный будильник
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            pendingIntent
        )
    }
}
