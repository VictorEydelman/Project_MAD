package mad.project.SleepMonitor.screens.abilities // Убедись, что пакет правильный

// Добавь все необходимые импорты из AllTimeContent.kt
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Divider
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.times

// Импорты общих определений и компонентов
import mad.project.SleepMonitor.ui.theme.White // Предполагая, что White доступен // Важно импортировать
import mad.project.SleepMonitor.ui.abilities.ButtonInactiveBackground
import mad.project.SleepMonitor.ui.abilities.LabelColor
import mad.project.SleepMonitor.ui.abilities.RingColor
import mad.project.SleepMonitor.ui.abilities.SleepDetailItem

private enum class SleepStage { AWAKE, REM, LIGHT, DEEP }

// Новые цвета для графика стадий
private val AwakeningColor = Color(0xFFA09EE8)
private val REMColor = Color(0xFF8A88D8)
private val LightSleepColor = Color(0xFF6C6AC0)
private val DeepSleepColor = Color(0xFF49478C)
private val SleepTimelineTrackColor = Color(0xFF3A395E)

@Composable
internal fun DayContent() {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // --- Первый блок (Кольцо и Информация о времени) ---
        // Используем актуальный код первого блока (как в AllTimeContent)
        Surface(
            modifier = Modifier.fillMaxWidth().height(160.dp),
            shape = RoundedCornerShape(18.dp),
            color = ButtonInactiveBackground
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Левый подблок (Кольцо)
                Column(
                    modifier = Modifier.width(110.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            // TODO: Заменить на реальные данные для Day
                            progress = { 0.90f }, // Пример для Day
                            modifier = Modifier.size(80.dp),
                            color = RingColor, // Цвет из общих
                            strokeWidth = 8.dp,
                            trackColor = Color.Transparent,
                            strokeCap = StrokeCap.Round
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("90", color = White, fontSize = 25.sp, fontWeight = FontWeight.SemiBold) // Пример
                            Text("Great", color = White, fontSize = 10.sp, fontWeight = FontWeight.SemiBold) // Пример
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
                    Column(horizontalAlignment = Alignment.Start) {
                        // TODO: Заменить на реальные данные для Day
                        Text("21:55 - 06:00", color = White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold) // Пример
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Time in bed", color = LabelColor, fontSize = 13.sp, fontWeight = FontWeight.Normal)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text("7h 30m", color = White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold) // Пример
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Time asleep", color = LabelColor, fontSize = 13.sp, fontWeight = FontWeight.Normal)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("06:05", color = White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold) // Пример
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Wakeup time", color = LabelColor, fontSize = 13.sp, fontWeight = FontWeight.Normal)
                        }
                    }
                }
            }
        }

        // --- Второй блок (Sleep Detail) ---
        // Используем актуальный код второго блока (как в AllTimeContent)
        Surface(
            modifier = Modifier.fillMaxWidth().height(170.dp),
            shape = RoundedCornerShape(18.dp),
            color = ButtonInactiveBackground
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text("Sleep Detail", color = White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold) // Изменен заголовок
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // TODO: Заменить на данные для Week
                    SleepDetailItem(value = "1", labelFirstLine = "Average", labelRest = "Number\nof awakenings") // Пример
                    SleepDetailItem(value = "10m", labelFirstLine = "Average", labelRest = "Duration\nof awakenings") // Пример
                    SleepDetailItem(value = "5m", labelFirstLine = "Average", labelRest = "Time\nto fall asleep") // Пример
                }
            }
        }

        // --- Третий блок (График стадий сна и сводка) ---
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp), // Оставляем увеличенную высоту
            shape = RoundedCornerShape(18.dp),
            color = ButtonInactiveBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Реалистичные данные для графика (8 часов, 22:00 - 06:00)
                val stageLabels = listOf("Awakening", "REM", "Light", "Deep")
                val stageData = remember { // Использование remember для стабильности при рекомпозиции
                    listOf(
                        // Awakening: 22:00-22:15, 04:00-04:10, 05:55-06:00 (30m)
                        listOf(Pair(0.0f, 0.03125f), Pair(0.75f, 0.77083f), Pair(0.98958f, 1.0f)),
                        // REM: 00:15-00:45, 03:15-04:00, 05:00-05:40 (115m)
                        listOf(Pair(0.28125f, 0.34375f), Pair(0.65625f, 0.75f), Pair(0.89583f, 0.95833f)),
                        // Light: 22:15-22:45, 23:30-00:15, 00:45-01:45, 02:30-03:15, 04:10-05:00, 05:40-05:55 (245m)
                        listOf(Pair(0.03125f, 0.09375f), Pair(0.1875f, 0.28125f), Pair(0.34375f, 0.46875f), Pair(0.5625f, 0.65625f), Pair(0.77083f, 0.89583f), Pair(0.95833f, 0.98958f)),
                        // Deep: 22:45-23:30, 01:45-02:30 (90m)
                        listOf(Pair(0.09375f, 0.1875f), Pair(0.46875f, 0.5625f))
                    )
                }
                val stageColors = listOf(AwakeningColor, REMColor, LightSleepColor, DeepSleepColor)

                SleepStagesTimeline( // Вызываем исправленную версию
                    stageLabels = stageLabels,
                    stageData = stageData,
                    stageColors = stageColors
                )
                Spacer(modifier = Modifier.height(32.dp))
                SleepStagesSummary() // Вызываем сводку
            }
        }
    }
}

@Composable
private fun SleepStagesTimeline(
    stageLabels: List<String>,
    stageData: List<List<Pair<Float, Float>>>, // List<List<Pair<start, end>>>
    stageColors: List<Color>,
    modifier: Modifier = Modifier
) {
    // Размеры
    val rowHeight = 35.dp // Общая высота для одной строки
    val barHeight = 28.dp // Высота самого бара
    // Общая высота всего блока с графиком (без меток времени)
    val totalGraphHeight = stageLabels.size * rowHeight

    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // --- Метки стадий слева ---
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

            // --- Область с полосами графика ---
            // Используем Column для рядов и Canvas для рисования
            Column(modifier = Modifier.weight(1f).height(totalGraphHeight)) {
                // Пространство для рисования займет всю доступную область Column
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val graphWidth = size.width // Полная ширина области рисования
                    val barTopOffset = (rowHeight.toPx() - barHeight.toPx()) / 2f // Вертикальный отступ бара

                    // Итерируем по каждой стадии (каждой полосе)
                    stageData.forEachIndexed { stageIndex, segments ->
                        // Вычисляем верхнюю Y-координату для текущей полосы
                        val rowTopY = stageIndex * rowHeight.toPx()
                        val barTopY = rowTopY + barTopOffset

                        // 1. Рисуем фон-трек для текущей стадии
                        drawRect(
                            color = SleepTimelineTrackColor,
                            topLeft = Offset(x = 0f, y = barTopY),
                            size = Size(width = graphWidth, height = barHeight.toPx())
                        )

                        // 2. Рисуем цветные сегменты поверх трека для текущей стадии
                        segments.forEach { (startFraction, endFraction) ->
                            val startPx = startFraction * graphWidth
                            val endPx = endFraction * graphWidth
                            val segmentWidth = endPx - startPx

                            // Пропускаем невалидные сегменты
                            if (segmentWidth > 0f) {
                                drawRect(
                                    color = stageColors[stageIndex], // Цвет текущей стадии
                                    topLeft = Offset(x = startPx, y = barTopY), // Та же Y-координата, что и у трека
                                    size = Size(width = segmentWidth, height = barHeight.toPx())
                                )
                            }
                        } // Конец forEach по сегментам
                    } // Конец forEachIndexed по стадиям
                } // Конец Canvas
            } // Конец Column для области графика
        } // Конец Row (Метки + График)

        // --- Метки времени под графиком ---
        Spacer(modifier = Modifier.height(4.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.width(78.dp)) // Отступ равен ширине меток + spacer
            Box(modifier = Modifier.weight(1f)) {
                Text("22:00", color = LabelColor, fontSize = 12.sp, modifier = Modifier.align(Alignment.CenterStart))
                Text("6:00", color = LabelColor, fontSize = 12.sp, modifier = Modifier.align(Alignment.CenterEnd))
            }
        }
    }
}

// SleepStagesSummary - СВОДКА С ДИАГРАММОЙ И СПИСКОМ
@Composable
private fun SleepStagesSummary(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Левая часть: Кольцевая диаграмма
        Box(
            modifier = Modifier.size(100.dp),
            contentAlignment = Alignment.Center
        ) {
            // Используем реалистичные доли, рассчитанные ранее
            val segmentData = listOf(
                Pair(AwakeningColor, 0.0625f),  // 30m
                Pair(REMColor, 0.2396f),       // 115m
                Pair(LightSleepColor, 0.5104f), // 245m
                Pair(DeepSleepColor, 0.1875f)   // 90m
            )
            val strokeWidth = 12.dp
            Canvas(modifier = Modifier.fillMaxSize().padding(strokeWidth / 2)) {
                var startAngle = -90f
                segmentData.forEach { (color, fraction) ->
                    // Убедимся, что fraction не отрицательный и не слишком большой
                    val safeFraction = fraction.coerceIn(0f, 1f)
                    // Убедимся, что sweep не выходит за 360 и не отрицательный
                    val sweep = (safeFraction * 360f).coerceAtMost(360f - (startAngle + 90f)).coerceAtLeast(0f)

                    drawArc(
                        color = color,
                        startAngle = startAngle,
                        sweepAngle = sweep,
                        useCenter = false,
                        style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Butt)
                    )
                    startAngle += sweep
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Правая часть: Список стадий и длительностей (с реалистичными данными)
        Column(modifier = Modifier.weight(1f)) {
            SleepStageDurationItem("Awakening", "0h 30m")
            SleepStageDurationItem("REM", "1h 55m")
            SleepStageDurationItem("Light", "4h 05m")
            SleepStageDurationItem("Deep", "1h 30m")
        }
    }
}

// Вспомогательный Composable для строки в списке длительностей
// Вспомогательный Composable для строки в списке длительностей
@Composable
private fun SleepStageDurationItem(stageName: String, duration: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp, horizontal = 40.dp), // Оставляем небольшой вертикальный отступ
        verticalAlignment = Alignment.CenterVertically
        // Убираем horizontalArrangement или оставляем Start (по умолчанию)
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

