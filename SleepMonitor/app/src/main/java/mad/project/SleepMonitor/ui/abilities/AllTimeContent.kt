package mad.project.SleepMonitor.ui.abilities

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember // Добавляем remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mad.project.SleepMonitor.domain.model.Report // Импорт модели Report
import mad.project.SleepMonitor.ui.theme.White
import java.time.Duration // Импорт Duration
import java.time.LocalTime // Импорт LocalTime
import java.time.format.DateTimeFormatter // Импорт форматтера
import java.time.format.FormatStyle // Импорт стилей форматтера


// Форматирует Duration в строку "Xh Ym" или "Ym"
fun formatDuration(duration: Duration?): String {
    return duration?.let {
        val hours = it.toHours()
        val minutes = it.toMinutesPart()
        when {
            hours > 0 -> "${hours}h ${minutes}m"
            minutes > 0 -> "${minutes}m"
            else -> "0m" // Или "--" если нужно показать отсутствие данных
        }
    } ?: "--" // Отображение для null
}

// Форматирует LocalTime в строку (например, "10:30 PM")
// Используем remember внутри Composable для форматтера

// Преобразует числовое качество в текстовое описание
fun mapQualityToText(quality: Int): String {
    return when {
        quality >= 85 -> "Great"
        quality >= 70 -> "Good"
        quality >= 55 -> "Okay"
        else -> "Poor"
    }
}


@Composable
internal fun AllTimeContent(report: Report) {

    // Создаем форматтер для времени внутри Composable с remember
    val timeFormatter = remember { DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT) }

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // --- Первый блок (Кольцо и Информация о времени) ---
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            shape = RoundedCornerShape(18.dp),
            color = ButtonInactiveBackground
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Левый подблок (Кольцо)
                Column(
                    modifier = Modifier.width(110.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        val qualityProgress = report.quality / 100f // Преобразуем Int в Float для прогресса
                        CircularProgressIndicator(
                            progress = { qualityProgress }, // Используем данные из отчета
                            modifier = Modifier.size(80.dp),
                            color = RingColor, // Убедитесь, что эти цвета определены
                            strokeWidth = 8.dp,
                            trackColor = Color.Transparent,
                            strokeCap = StrokeCap.Round
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = report.quality.toString(), // Используем данные из отчета
                                color = White,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = mapQualityToText(report.quality), // Используем данные из отчета
                                color = White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    // Для "All Time" логично показывать среднее качество
                    Text("Average Quality", color = White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.width(16.dp))


                // Форматируем время начала и конца
                val startTimeStr = report.startTime?.format(timeFormatter) ?: "--:--"
                val endTimeStr = report.endTime?.format(timeFormatter) ?: "--:--"

                // Правый подблок (Время)
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.Start) {

                        Text(
                            text = "$startTimeStr - $endTimeStr",
                            color = White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        // Возможно, "Average Time in bed" для All Time? Или оставить так.
                        Text("Time in bed", color = LabelColor, fontSize = 13.sp, fontWeight = FontWeight.Normal)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                // Для All Time используем среднее время сна
                                text = formatDuration(report.avgAsleep),
                                color = White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Avg time asleep", color = LabelColor, fontSize = 13.sp, fontWeight = FontWeight.Normal)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                // Используем отформатированное время конца
                                text = endTimeStr,
                                color = White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Avg wakeup time", color = LabelColor, fontSize = 13.sp, fontWeight = FontWeight.Normal)
                        }
                    }
                }
            }
        }

        // --- Второй блок (Sleep Detail) ---
        Surface(
            modifier = Modifier.fillMaxWidth().height(170.dp),
            shape = RoundedCornerShape(18.dp),
            color = ButtonInactiveBackground
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                // Уточняем заголовок для All Time
                Text("Sleep Detail", color = White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(0.9f), // Занимает 90% ширины родителя (внешнего Row)
                        horizontalArrangement = Arrangement.SpaceBetween // Элементы распределяются внутри этих 90%
                    ) {
                        // Используем импортированный SleepDetailItem
                        SleepDetailItem(
                            value = report.awakenings?.toString() ?: "--", // Обрабатываем null
                            labelFirstLine = "Average",
                            labelRest = "Number\nof awakenings"
                        )
                        SleepDetailItem(
                            value = formatDuration(report.avgAwake), // Используем среднее время пробуждений
                            labelFirstLine = "Average",
                            labelRest = "Duration\nof awakenings"
                        )
                        SleepDetailItem(
                            value = formatDuration(report.avgToFallAsleep), // Используем среднее время засыпания (nullable)
                            labelFirstLine = "Average",
                            labelRest = "Time\nto fall asleep"
                        )
                    }
                }
            }
        }
    }
}


