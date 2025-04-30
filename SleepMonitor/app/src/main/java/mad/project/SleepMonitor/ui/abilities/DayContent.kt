package mad.project.SleepMonitor.screens.abilities

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mad.project.SleepMonitor.domain.model.Report
import mad.project.SleepMonitor.domain.model.SleepData
import mad.project.SleepMonitor.domain.model.SleepPhase
import mad.project.SleepMonitor.ui.abilities.*
import mad.project.SleepMonitor.ui.theme.White
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.max

// Цвета для графика стадий
private val AwakeningColor = Color(0xFFA09EE8)
private val REMColor = Color(0xFF8A88D8)
private val LightSleepColor = Color(0xFF6C6AC0)
private val DeepSleepColor = Color(0xFF49478C)
private val SleepTimelineTrackColor = Color(0xFF3A395E)

// --- Структура для подготовленных данных графика ---
private data class PreparedTimelineData(
    val segmentsByPhase: Map<SleepPhase, List<Pair<Float, Float>>>,
    val startInstant: LocalDateTime?,
    val endInstant: LocalDateTime?
)

private data class PreparedSummaryData(
    val durationByPhase: Map<SleepPhase, Duration>,
    val totalDuration: Duration
)

@Composable
internal fun DayContent(report: Report) {
    val timeFormatter = remember { DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT) }

    val timelineData = remember(report.data) {
        prepareTimelineData(report.data)
    }
    val summaryData = remember(report.data) {
        prepareSummaryData(report.data)
    }

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
                    Text("Sleep Quality", color = White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
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
                            text = "$startTimeStr - $endTimeStr",
                            color = White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Time in bed", color = LabelColor, fontSize = 13.sp, fontWeight = FontWeight.Normal)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                text = formatDuration(report.totalSleep),
                                color = White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Time asleep", color = LabelColor, fontSize = 13.sp, fontWeight = FontWeight.Normal)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = endTimeStr,
                                color = White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Wakeup time", color = LabelColor, fontSize = 13.sp, fontWeight = FontWeight.Normal)
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

        // --- Третий блок (График стадий сна и сводка) --
        println("DayContent: timelineData.startInstant = ${timelineData.startInstant}")
        println("DayContent: timelineData.endInstant = ${timelineData.endInstant}")
        println("DayContent: summaryData.totalDuration = ${summaryData.totalDuration}")
        val conditionMet = timelineData.startInstant != null
                && timelineData.endInstant != null
                && summaryData.totalDuration > Duration.ZERO
        println("DayContent: if condition met = $conditionMet")

        if (conditionMet) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp),
                shape = RoundedCornerShape(18.dp),
                color = ButtonInactiveBackground
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    val orderedPhases = listOf(SleepPhase.AWAKE, SleepPhase.REM, SleepPhase.LIGHT, SleepPhase.DEEP)
                    val stageLabels = orderedPhases.map { it.name.capitalizeFirst() } // "AWAKE" -> "Awake"
                    val stageColors = orderedPhases.map { getPhaseColor(it) }
                    val stageData = orderedPhases.map {
                        timelineData.segmentsByPhase[it] ?: emptyList()
                    }

                    println("stageLabels = ${stageLabels}")
                    SleepStagesTimeline(
                        stageLabels = stageLabels,
                        stageData = stageData,
                        stageColors = stageColors,
                        startTimeLabel = timelineData.startInstant?.atZone(ZoneId.systemDefault())?.format(timeFormatter) ?: "--:--",
                        endTimeLabel = timelineData.endInstant?.atZone(ZoneId.systemDefault())?.format(timeFormatter) ?: "--:--"
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    SleepStagesSummary(
                        summaryData = summaryData,
                        orderedPhases = orderedPhases
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
                    Text("No detailed sleep data available for this day.", color=LabelColor)
                }
            }
        }
    }
}


@Composable
private fun SleepStagesTimeline(
    stageLabels: List<String>,
    stageData: List<List<Pair<Float, Float>>>,
    stageColors: List<Color>,
    startTimeLabel: String,
    endTimeLabel: String,
    modifier: Modifier = Modifier
) {
    // Размеры
    val rowHeight = 35.dp
    val barHeight = 28.dp
    val totalGraphHeight = rowHeight * stageLabels.size

    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Метки стадий слева
            Column(modifier = Modifier.width(70.dp)) {
                stageLabels.forEach { label ->
                    Box(
                        modifier = Modifier.height(rowHeight),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = label,
                            color = LabelColor,
                            fontSize = 12.sp,
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(8.dp))

            // Область с полосами графика
            Column(modifier = Modifier.weight(1f).height(totalGraphHeight)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val graphWidth = size.width
                    val barTopOffset = (rowHeight.toPx() - barHeight.toPx()) / 2f

                    stageData.forEachIndexed { stageIndex, segments ->
                        val rowTopY = stageIndex * rowHeight.toPx()
                        val barTopY = rowTopY + barTopOffset

                        // Рисуем фон-трек
                        drawRect(
                            color = SleepTimelineTrackColor,
                            topLeft = Offset(x = 0f, y = barTopY),
                            size = Size(width = graphWidth, height = barHeight.toPx())
                        )

                        // Рисуем сегменты
                        segments.forEach { (startFraction, endFraction) ->
                            val startPx = startFraction * graphWidth
                            val endPx = endFraction * graphWidth
                            val segmentWidth = (endPx - startPx).coerceAtLeast(0f)

                            if (segmentWidth > 0.1f) {
                                drawRect(
                                    color = stageColors[stageIndex],
                                    topLeft = Offset(x = startPx, y = barTopY),
                                    size = Size(width = segmentWidth, height = barHeight.toPx())
                                )
                            }
                        }
                    }
                }
            }
        }

        // Метки времени под графиком
        Spacer(modifier = Modifier.height(4.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.width(78.dp))
            Box(modifier = Modifier.weight(1f)) {
                Text(
                    text = startTimeLabel,
                    color = LabelColor,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                Text(
                    text = endTimeLabel,
                    color = LabelColor,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
    }
}

@Composable
private fun SleepStagesSummary(
    summaryData: PreparedSummaryData,
    orderedPhases: List<SleepPhase>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Левая часть: Кольцевая диаграмма
        Box(
            modifier = Modifier.size(100.dp),
            contentAlignment = Alignment.Center
        ) {
            val strokeWidth = 12.dp
            Canvas(modifier = Modifier.fillMaxSize().padding(strokeWidth / 2)) {
                println("--- Drawing Summary Canvas ---")
                var drawnSomething = false

                var startAngle = -90f

                orderedPhases.forEach { phase ->
                    val duration = summaryData.durationByPhase[phase] ?: Duration.ZERO
                    val totalDurationMs = max(summaryData.totalDuration.toMillis(), 1L)
                    val fraction = duration.toMillis().toFloat() / totalDurationMs.toFloat()
                    val sweep = (fraction * 360f).coerceIn(0f, 360f)

                    println("Phase: $phase, Duration: $duration, Fraction: $fraction, Sweep: $sweep")

                    if (sweep > 0.1f) {
                        drawnSomething = true
                        drawArc(
                            color = getPhaseColor(phase),
                            startAngle = startAngle,
                            sweepAngle = sweep,
                            useCenter = false,
                            style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Butt)
                        )
                        startAngle += sweep
                    }
                }

                if (!drawnSomething) {
                    println("!!! Drawing fallback red circle !!!")
                    drawCircle(color = Color.Red, style = Stroke(width = strokeWidth.toPx()))
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Правая часть: Список стадий и длительностей
        Column(modifier = Modifier.weight(1f)) {
            orderedPhases.forEach { phase ->
                val duration = summaryData.durationByPhase[phase] ?: Duration.ZERO
                SleepStageDurationItem(
                    stageName = phase.name.capitalizeFirst(),
                    duration = formatDuration(duration)
                )
            }
        }
    }
}


@Composable
private fun SleepStageDurationItem(stageName: String, duration: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp, horizontal = 40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f)
        ){
            // Текст для названия стадии
            Text(
                text = stageName,
                color = LabelColor,
                fontSize = 12.sp,
                modifier = Modifier.weight(1f)
            )

            // Текст для длительности
            Text(
                text = duration,
                color = LabelColor,
                fontSize = 12.sp,
                textAlign = TextAlign.End
            )
        }

    }
}

private fun prepareTimelineData(sleepData: SleepData?): PreparedTimelineData {
    if (sleepData.isNullOrEmpty() || sleepData.size < 2) {
        return PreparedTimelineData(emptyMap(), null, null)
    }

    val sortedData = sleepData.sortedBy { it.timestamp }
    val minTimestamp = sortedData.first().timestamp
    val maxTimestamp = sortedData.last().timestamp
    val totalDurationSeconds = Duration.between(minTimestamp, maxTimestamp).seconds

    if (totalDurationSeconds <= 0) {
        return PreparedTimelineData(emptyMap(), minTimestamp, maxTimestamp)
    }

    val segments = mutableMapOf<SleepPhase, MutableList<Pair<Float, Float>>>()

    for (i in 0 until sortedData.size - 1) {
        val p1 = sortedData[i]
        val p2 = sortedData[i + 1]
        val phase = p1.sleepPhase // Фаза определяется по началу интервала

        val startSeconds = Duration.between(minTimestamp, p1.timestamp).seconds
        val endSeconds = Duration.between(minTimestamp, p2.timestamp).seconds

        // Пропускаем слишком длинные разрывы между точками (например, больше часа)
        if (endSeconds - startSeconds > 3600) continue

        val startFraction = startSeconds.toFloat() / totalDurationSeconds.toFloat()
        val endFraction = endSeconds.toFloat() / totalDurationSeconds.toFloat()

        // Добавляем сегмент к соответствующей фазе
        segments.computeIfAbsent(phase) { mutableListOf() }.add(Pair(startFraction, endFraction))
    }

    // Объединяем соседние сегменты одной фазы (опционально, для чистоты)
    val mergedSegments = segments.mapValues { (_, phaseSegments) -> mergeAdjacentSegments(phaseSegments) }


    return PreparedTimelineData(mergedSegments, minTimestamp, maxTimestamp)
}

// Опциональная функция для склейки близких сегментов
private fun mergeAdjacentSegments(segments: List<Pair<Float, Float>>, threshold: Float = 0.001f): List<Pair<Float, Float>> {
    if (segments.isEmpty()) return emptyList()
    val sorted = segments.sortedBy { it.first }
    val merged = mutableListOf<Pair<Float, Float>>()
    var current = sorted.first()
    for (i in 1 until sorted.size) {
        val next = sorted[i]
        if (next.first <= current.second + threshold) { // Если следующий начинается близко к концу текущего
            current = Pair(current.first, maxOf(current.second, next.second)) // Объединяем
        } else {
            merged.add(current) // Завершаем текущий
            current = next      // Начинаем новый
        }
    }
    merged.add(current) // Добавляем последний сегмент
    return merged
}


private fun prepareSummaryData(sleepData: SleepData?): PreparedSummaryData {
    if (sleepData.isNullOrEmpty() || sleepData.size < 2) {
        return PreparedSummaryData(emptyMap(), Duration.ZERO)
    }

    val durationByPhase = mutableMapOf<SleepPhase, Duration>()
    var totalSleepDuration = Duration.ZERO // Считаем только фазы сна
    val sortedData = sleepData.sortedBy { it.timestamp }

    for (i in 0 until sortedData.size - 1) {
        val p1 = sortedData[i]
        val p2 = sortedData[i + 1]
        val phase = p1.sleepPhase
        val durationBetweenPoints = Duration.between(p1.timestamp, p2.timestamp)

        // Пропускаем слишком длинные разрывы
        if (durationBetweenPoints.abs().toMinutes() > 60) continue

        // Добавляем длительность к соответствующей фазе
        durationByPhase[phase] = durationByPhase.getOrDefault(phase, Duration.ZERO).plus(durationBetweenPoints)

        // Если это фаза сна (не AWAKE), добавляем к общей длительности сна
        if (phase != SleepPhase.AWAKE) {
            totalSleepDuration = totalSleepDuration.plus(durationBetweenPoints)
        }
    }

    // Используем totalSleepDuration для расчета долей в кольцевой диаграмме
    return PreparedSummaryData(durationByPhase, totalSleepDuration)
}

// Получение цвета по фазе
private fun getPhaseColor(phase: SleepPhase): Color {
    return when (phase) {
        SleepPhase.AWAKE -> AwakeningColor
        SleepPhase.REM -> REMColor
        SleepPhase.LIGHT -> LightSleepColor
        SleepPhase.DEEP -> DeepSleepColor
        SleepPhase.UNKNOWN -> Color.Gray
    }
}

// Расширение для String.capitalize()
private fun String.capitalizeFirst(): String {
    return this.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}