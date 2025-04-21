package mad.project.SleepMonitor.screens // Основной пакет экрана

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import mad.project.SleepMonitor.screens.abilities.DayContent
import mad.project.SleepMonitor.screens.abilities.WeekContent
import mad.project.SleepMonitor.ui.common.AppScaffold
import mad.project.SleepMonitor.ui.theme.White

// Импортируем компоненты и определения из нового пакета
import mad.project.SleepMonitor.ui.abilities.TimeRange
import mad.project.SleepMonitor.ui.abilities.TimeRangeButton
import mad.project.SleepMonitor.ui.abilities.AllTimeContent

@Composable
fun AbilitiesScreen(navController: NavController) { // Убираем private, если экран вызывается из навигации

    var selectedTimeRange by remember { mutableStateOf(TimeRange.ALL) }

    AppScaffold(navController = navController) {

        // --- Хедер ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Назад",
                tint = White,
                modifier = Modifier.size(28.dp).clickable { navController.navigateUp() }
            )
            Spacer(modifier = Modifier.width(32.dp))
            Text(
                text = "Statistic",
                color = White,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        // --- Ряд Кнопок ---
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Используем импортированный TimeRangeButton
            TimeRangeButton(
                text = "All time",
                timeRange = TimeRange.ALL,
                isSelected = selectedTimeRange == TimeRange.ALL,
                onClick = { selectedTimeRange = TimeRange.ALL },
                modifier = Modifier.weight(1f)
            )
            TimeRangeButton(
                text = "Week",
                timeRange = TimeRange.WEEK,
                isSelected = selectedTimeRange == TimeRange.WEEK,
                onClick = { selectedTimeRange = TimeRange.WEEK },
                modifier = Modifier.weight(1f)
            )
            TimeRangeButton(
                text = "Day",
                timeRange = TimeRange.DAY,
                isSelected = selectedTimeRange == TimeRange.DAY,
                onClick = { selectedTimeRange = TimeRange.DAY },
                modifier = Modifier.weight(1f)
            )
        }

        // --- Основной контент ---
        Spacer(modifier = Modifier.height(30.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            // Вызываем импортированные функции контента
            when (selectedTimeRange) {
                TimeRange.ALL -> AllTimeContent()
                TimeRange.WEEK -> WeekContent()
                TimeRange.DAY -> DayContent()
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}