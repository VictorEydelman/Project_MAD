package mad.project.SleepMonitor.ui.abilities

import androidx.compose.ui.graphics.Color
import mad.project.SleepMonitor.ui.theme.White // Убедись, что White импортирован правильно

// Enum для кнопок выбора временного диапазона
enum class TimeRange {
    ALL, WEEK, DAY
}

// Общие цвета, используемые на экране Abilities (Statistics)
internal val ButtonInactiveBackground = Color(0xFF313050)
internal val ButtonActiveBackground = White
internal val ButtonInactiveText = White
internal val ButtonActiveText = Color(0xFF313050)
internal val RingColor = Color(0xFF3C5195)
internal val LabelColor = White.copy(alpha = 0.7f)

