package mad.project.SleepMonitor.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mad.project.SleepMonitor.components.*
import mad.project.SleepMonitor.notification.NotificationService
import mad.project.SleepMonitor.viewmodels.BedTimeViewModel
import java.util.Calendar

@Composable
fun BedTimeScreen(navController: NavController, notificationService: NotificationService) {
    val alarmViewModel: BedTimeViewModel = viewModel(LocalContext.current as ComponentActivity)
    val sleepReminderEnabledState by alarmViewModel.isSleepReminderEnabled.collectAsState()
    val bedReminderEnabledState by alarmViewModel.isBedReminderEnabled.collectAsState()
    val repeatEnabledState by alarmViewModel.isRepeatEnabled.collectAsState()

    var selectedTime by remember { mutableStateOf("") }

    Surface(
        color = Color(0xFF011222),
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF011222))
            .padding(40.dp, 70.dp, 40.dp, 35.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowLeft,
                    contentDescription = "Arrow Left",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp).clickable { navController.navigate("main_screen") }
                )
                SubTitleTextComponent(value = "Bed Time")
            }
            TimePicker { time -> selectedTime = time }
            Spacer(modifier = Modifier.height(20.dp))
            // "Remind me to sleep"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFF313050), shape = RoundedCornerShape(20.dp))
                    .padding(20.dp, 10.dp, 50.dp, 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NormalTextComponent(value = "Remind me to sleep")
                SettingSwitch(
                    checked = sleepReminderEnabledState,
                    onCheckedChange = { alarmViewModel.setSleepReminderEnabled(it) }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            // "Remind before bed"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFF313050), shape = RoundedCornerShape(20.dp))
                    .padding(20.dp, 10.dp, 50.dp, 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NormalTextComponent(value = "Remind before bed")
                SettingSwitch(
                    checked = bedReminderEnabledState,
                    onCheckedChange = { alarmViewModel.setBedReminderEnabled(it) }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            // "Repeat every day"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFF313050), shape = RoundedCornerShape(20.dp))
                    .padding(20.dp, 10.dp, 50.dp, 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NormalTextComponent(value = "Repeat every day")
                SettingSwitch(
                    checked = repeatEnabledState,
                    onCheckedChange = { alarmViewModel.setRepeatEnabled(it) }
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            // "Set Bed Time"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, color = Color(0xFF313050), shape = RoundedCornerShape(20.dp))
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .clickable {
                        val parts = selectedTime.split(":")
                        if (parts.size == 2) {
                            val hour = parts[0].toIntOrNull() ?: 0
                            val minute = parts[1].toIntOrNull() ?: 0
                            scheduleBedTimeNotification(
                                notificationService,
                                hour,
                                minute,
                                sleepReminderEnabledState,
                                bedReminderEnabledState,
                                repeatEnabledState
                            )
                        }
                        navController.navigate("main_screen")
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SubTitleTextComponent(value = "Set Bed Time")
            }
        }
    }
}

private fun scheduleBedTimeNotification(
    notificationService: NotificationService,
    hour: Int,
    minute: Int,
    sleepReminder: Boolean,
    bedReminder: Boolean,
    repeat: Boolean
) {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)

        if (timeInMillis <= System.currentTimeMillis()) {
            add(Calendar.DAY_OF_MONTH, 1)
        }
    }
    if (repeat) {
        notificationService.scheduleRepeatingNotification(calendar.timeInMillis)
    } else {
        notificationService.scheduleNotification(calendar.timeInMillis)
    }
    if (sleepReminder) {
        notificationService.scheduleNotification(calendar.timeInMillis)
    }
    if (bedReminder) {
        notificationService.scheduleNotification(calendar.timeInMillis)
    }
}
