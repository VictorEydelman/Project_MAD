package mad.project.SleepMonitor.screens // Основной пакет экрана

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import mad.project.SleepMonitor.viewmodels.AnalyticsViewModel

@Composable
fun AnalyticsScreen(navController: NavController, viewModel: AnalyticsViewModel) { // Убираем private, если экран вызывается из навигации

    val state by viewModel.state.collectAsState()
    val selectedTimeRange = state.selectedTimeRange

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
                text = "Analytics",
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
                onClick = { viewModel.onTimeRangeSelected(TimeRange.ALL) },
                modifier = Modifier.weight(1f)
            )
            TimeRangeButton(
                text = "Week",
                timeRange = TimeRange.WEEK,
                isSelected = selectedTimeRange == TimeRange.WEEK,
                onClick = { viewModel.onTimeRangeSelected(TimeRange.WEEK) },
                modifier = Modifier.weight(1f)
            )
            TimeRangeButton(
                text = "Day",
                timeRange = TimeRange.DAY,
                isSelected = selectedTimeRange == TimeRange.DAY,
                onClick = { viewModel.onTimeRangeSelected(TimeRange.DAY) },
                modifier = Modifier.weight(1f)
            )
        }

        // --- Основной контент ---
        Spacer(modifier = Modifier.height(30.dp))

        Box( // Используем Box для удобного отображения загрузки/ошибки по центру
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.TopCenter // Выравниваем контент по верху
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                state.error != null -> {
                    Text(
                        text = "Error: ${state.error}",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.report != null -> {
                    // Показываем контент, если данные есть
                    Column(modifier = Modifier.fillMaxWidth()) {
                        when (selectedTimeRange) {
                            // Передаем данные отчета в соответствующие Composable
                            TimeRange.ALL -> AllTimeContent(report = state.report!!)
                            TimeRange.WEEK -> WeekContent(report = state.report!!)
                            TimeRange.DAY -> DayContent(report = state.report!!)
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
                else -> {
                    // Состояние по умолчанию или если отчет пуст, но нет ошибки
                    Text(
                        text = "No data available for the selected period.",
                        color = White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}