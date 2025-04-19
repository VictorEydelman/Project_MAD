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
import mad.project.SleepMonitor.ui.abilities.ButtonInactiveBackground
import mad.project.SleepMonitor.ui.abilities.LabelColor
import mad.project.SleepMonitor.ui.abilities.RingColor
import mad.project.SleepMonitor.ui.abilities.SleepDetailItem
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background

// Импорты общих определений и компонентов
import mad.project.SleepMonitor.ui.theme.White // Предполагая, что White доступен

private val BarChartTrackColor = Color(0xFF4A4A6A)
private val BarChartFilledColor = Color(0xFF8A88D8)

@Composable
internal fun WeekContent() { // internal
    // Копируем содержимое из AllTimeContent
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
                        CircularProgressIndicator(
                            // TODO: Заменить на данные для Week
                            progress = { 0.75f }, // Пример для Week
                            modifier = Modifier.size(80.dp),
                            color = RingColor,
                            strokeWidth = 8.dp,
                            trackColor = Color.Transparent,
                            strokeCap = StrokeCap.Round
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            // TODO: Заменить на данные для Week
                            Text("75", color = White, fontSize = 25.sp, fontWeight = FontWeight.SemiBold)
                            Text("Okay", color = White, fontSize = 10.sp, fontWeight = FontWeight.SemiBold) // Пример
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
                        // TODO: Заменить на данные для Week
                        Text("22.30 PM - 05.00 AM", color = White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold) // Пример
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Time in bed", color = LabelColor, fontSize = 13.sp, fontWeight = FontWeight.Normal)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            // TODO: Заменить на данные для Week
                            Text("6hr 15m", color = White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold) // Пример
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Time asleep", color = LabelColor, fontSize = 13.sp, fontWeight = FontWeight.Normal)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            // TODO: Заменить на данные для Week
                            Text("05.05", color = White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold) // Пример
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Wakeup time", color = LabelColor, fontSize = 13.sp, fontWeight = FontWeight.Normal)
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
                Text("Sleep Detail", color = White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold) // Изменен заголовок
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // TODO: Заменить на данные для Week
                    SleepDetailItem(value = "3", labelFirstLine = "Average", labelRest = "Number\nof awakenings") // Пример
                    SleepDetailItem(value = "18m", labelFirstLine = "Average", labelRest = "Duration\nof awakenings") // Пример
                    SleepDetailItem(value = "15m", labelFirstLine = "Average", labelRest = "Time\nto fall asleep") // Пример
                }
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                // Подберем высоту для графика
                .height(220.dp),
            shape = RoundedCornerShape(18.dp),
            color = ButtonInactiveBackground
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 16.dp) // Внутренние отступы
            ) {
                // --- Y-ось (Метки часов) ---
                YAxisLabels() // Вынесем в отдельную функцию для читаемости

                Spacer(modifier = Modifier.width(8.dp)) // Отступ между осью и графиком

                // --- Область с барами графика ---
                WeeklyBarChart() // Вынесем в отдельную функцию
            }
        }
    }
}


@Composable
private fun YAxisLabels(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(bottom = 20.dp), // Отступ снизу, чтобы 0h был на уровне названий дней
        verticalArrangement = Arrangement.SpaceBetween, // Распределяем метки равномерно
        horizontalAlignment = Alignment.End // Выравниваем текст по правому краю
    ) {
        // Новые метки
        Text("12 h", color = LabelColor, fontSize = 12.sp)
        Text("8 h", color = LabelColor, fontSize = 12.sp)
        Text("6 h", color = LabelColor, fontSize = 12.sp) // Добавлено
        Text("4 h", color = LabelColor, fontSize = 12.sp)
        Text("2 h", color = LabelColor, fontSize = 12.sp) // Добавлено
        Text("0 h", color = LabelColor, fontSize = 12.sp)
    }
}



@Composable
private fun RowScope.WeeklyBarChart(modifier: Modifier = Modifier) {
    // Примерные данные (часы сна для каждого дня)
    val sleepData = listOf(7.5f, 6.0f, 5.5f, 6.5f, 5.0f, 6.2f, 8.0f)
    val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val maxHours = 12f // Максимальное значение на оси Y

    Row(
        modifier = modifier
            .fillMaxSize() // Занимаем все доступное место в родительском Row
            .padding(start = 4.dp), // Небольшой отступ слева от первого бара
        horizontalArrangement = Arrangement.SpaceAround, // Распределяем бары
        verticalAlignment = Alignment.Bottom // Выравниваем все по низу (важно для текста под барами)
    ) {
        sleepData.forEachIndexed { index, sleepHours ->
            BarColumn(
                value = sleepHours,
                maxValue = maxHours,
                label = days[index]
            )
        }
    }
}

// Composable для одного столбца графика (бар + подпись)
@Composable
private fun BarColumn(
    value: Float,       // Текущее значение (часы сна)
    maxValue: Float,    // Максимальное значение на графике (e.g., 12f)
    label: String,      // Подпись под баром ("Sun", "Mon", ...)
    modifier: Modifier = Modifier,
    barWidth: Dp = 20.dp, // Ширина столбца
    chartHeight: Dp = 160.dp // Высота области самого столбца (без подписи)
) {
    val barFillFraction = (value / maxValue).coerceIn(0f, 1f) // Доля заполнения от 0 до 1

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom // Выравниваем по низу
    ) {
        // Бокс для самого столбца
        Box(
            modifier = Modifier
                .width(barWidth)
                .height(chartHeight) // Общая высота области бара
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)) // Скругляем верх
                .background(BarChartTrackColor) // Фон/трек
        ) {
            // Бокс для заполненной части
            Box(
                modifier = Modifier
                    .fillMaxWidth() // Ширина как у родителя
                    .fillMaxHeight(fraction = barFillFraction) // Заполняем нужную долю высоты
                    .background(BarChartFilledColor) // Цвет заполнения
                    .align(Alignment.BottomCenter) // Прижимаем к низу
            )
        }
        Spacer(modifier = Modifier.height(4.dp)) // Отступ между баром и подписью
        // Подпись под столбцом
        Text(
            text = label,
            color = LabelColor,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}
