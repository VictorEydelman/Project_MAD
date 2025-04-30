package mad.project.SleepMonitor.screens.abilities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mad.project.SleepMonitor.domain.model.Report
import mad.project.SleepMonitor.domain.model.Weekday
import mad.project.SleepMonitor.ui.abilities.*
import mad.project.SleepMonitor.ui.theme.White
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


private val BarChartTrackColor = Color(0xFF4A4A6A)
private val BarChartFilledColor = Color(0xFF8A88D8)

// Максимальное значение для оси Y графика
private const val BAR_CHART_MAX_HOURS = 12f

@Composable
internal fun WeekContent(report: Report) {

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
                        val qualityProgress = report.quality / 100f
                        CircularProgressIndicator(
                            progress = { qualityProgress },
                            modifier = Modifier.size(80.dp),
                            color = RingColor,
                            strokeWidth = 8.dp,
                            trackColor = Color.Transparent,
                            strokeCap = StrokeCap.Round
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = report.quality.toString(),
                                color = White,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = mapQualityToText(report.quality),
                                color = White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Average Quality", color = White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold) // +++
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Правый подблок (Время)
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    val startTimeStr = report.startTime?.format(timeFormatter) ?: "--:--"
                    val endTimeStr = report.endTime?.format(timeFormatter) ?: "--:--"

                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            // Показываем среднее время начала/конца за период
                            text = "$startTimeStr - $endTimeStr", // +++
                            color = White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Avg time in bed", color = LabelColor, fontSize = 13.sp, fontWeight = FontWeight.Normal) // +++
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                text = formatDuration(report.avgAsleep),
                                color = White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Avg time asleep", color = LabelColor, fontSize = 13.sp, fontWeight = FontWeight.Normal) // +++
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = endTimeStr,
                                color = White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Avg wakeup time", color = LabelColor, fontSize = 13.sp, fontWeight = FontWeight.Normal) // +++
                        }
                    }
                }
            }
        }

        // --- Второй блок (Sleep Detail) ---
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp),
            shape = RoundedCornerShape(18.dp),
            color = ButtonInactiveBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text("Sleep Detail", color = White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SleepDetailItem(
                            value = report.awakenings?.toString() ?: "--",
                            labelFirstLine = "Average",
                            labelRest = "Number\nof awakenings"
                        )
                        SleepDetailItem(
                            value = formatDuration(report.avgAwake),
                            labelFirstLine = "Average",
                            labelRest = "Duration\nof awakenings"
                        )
                        SleepDetailItem(
                            value = formatDuration(report.avgToFallAsleep),
                            labelFirstLine = "Average",
                            labelRest = "Time\nto fall asleep"
                        )
                    }
                }
            }
        }

        // --- Третий блок (График) ---
        if (!report.distribution.isNullOrEmpty()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                shape = RoundedCornerShape(18.dp),
                color = ButtonInactiveBackground
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    YAxisLabels()

                    Spacer(modifier = Modifier.width(8.dp))


                    val orderedDays = listOf(
                        Weekday.Sun, Weekday.Mon, Weekday.Tue, Weekday.Wed, Weekday.Thu, Weekday.Fri, Weekday.Sat
                    )

                    val hoursMap = report.distribution.associateBy({ it.weekday }, { it.asleepHours })

                    val sleepDataHours = orderedDays.map { weekday ->
                        (hoursMap[weekday] ?: 0.0).toFloat()
                    }

                    val dayLabels = orderedDays.map { mapWeekdayToLabel(it) } // "Sun", "Mon", ...


                    WeeklyBarChart(
                        modifier = Modifier.weight(1f),
                        sleepData = sleepDataHours,
                        dayLabels = dayLabels
                    )
                }
            }
        } else {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(18.dp),
                color = ButtonInactiveBackground
            ){
                Box(contentAlignment = Alignment.Center){
                    Text("No weekly distribution data available.", color=LabelColor)
                }
            }
        }
    }
}

// Метки оси Y
@Composable
private fun YAxisLabels(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(bottom = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.End
    ) {
        Text("12 h", color = LabelColor, fontSize = 12.sp)
        Text("8 h", color = LabelColor, fontSize = 12.sp)
        Text("6 h", color = LabelColor, fontSize = 12.sp)
        Text("4 h", color = LabelColor, fontSize = 12.sp)
        Text("2 h", color = LabelColor, fontSize = 12.sp)
        Text("0 h", color = LabelColor, fontSize = 12.sp)
    }
}


@Composable
private fun RowScope.WeeklyBarChart(
    sleepData: List<Float>,
    dayLabels: List<String>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 4.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom
    ) {
        sleepData.forEachIndexed { index, sleepHours ->
            if (index < dayLabels.size) {
                BarColumn(
                    value = sleepHours,
                    maxValue = BAR_CHART_MAX_HOURS,
                    label = dayLabels[index]
                )
            }
        }
    }
}

// Столбец графика
@Composable
private fun BarColumn(
    value: Float,
    maxValue: Float,
    label: String,
    modifier: Modifier = Modifier,
    barWidth: Dp = 20.dp,
    chartHeight: Dp = 160.dp // Эта высота определяет максимальную высоту бара *относительно* низа колонки
) {
    val barFillFraction = (value / maxValue).coerceIn(0f, 1f)

    Column(
        modifier = modifier.height(chartHeight + 20.dp), // Общая высота колонки = высота бара + высота текста + отступ
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .width(barWidth)
                .height(chartHeight) // Область для рисования бара
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                .background(BarChartTrackColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraction = barFillFraction) // Заполняем долю
                    .background(BarChartFilledColor)
                    .align(Alignment.BottomCenter) // Растем снизу вверх
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = LabelColor,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}

// --- Вспомогательная функция для маппинга Weekday -> String ---
private fun mapWeekdayToLabel(weekday: Weekday): String {
    return when (weekday) {
        Weekday.Sun -> "Sun"
        Weekday.Mon -> "Mon"
        Weekday.Tue -> "Tue"
        Weekday.Wed -> "Wed"
        Weekday.Thu -> "Thu"
        Weekday.Fri -> "Fri"
        Weekday.Sat -> "Sat"
    }
}