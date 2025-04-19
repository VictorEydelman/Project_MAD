package mad.project.SleepMonitor.ui.abilities

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

// Импортируем общие определения и компоненты
import mad.project.SleepMonitor.ui.theme.White

@Composable
internal fun AllTimeContent() { // internal
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
                            progress = { 0.82f },
                            modifier = Modifier.size(80.dp),
                            color = RingColor,
                            strokeWidth = 8.dp,
                            trackColor = Color.Transparent,
                            strokeCap = StrokeCap.Round
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("82", color = White, fontSize = 25.sp, fontWeight = FontWeight.SemiBold)
                            Text("Good", color = White, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
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
                        Text("22.05 PM - 04.10 AM", color = White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Time in bed", color = LabelColor, fontSize = 13.sp, fontWeight = FontWeight.Normal)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text("5hr 54m", color = White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Time asleep", color = LabelColor, fontSize = 13.sp, fontWeight = FontWeight.Normal)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("04.15", color = White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
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
                Text("Sleep Detail", color = White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SleepDetailItem(value = "2", labelFirstLine = "Average", labelRest = "Number\nof awakenings")
                    SleepDetailItem(value = "25m", labelFirstLine = "Average", labelRest = "Duration\nof awakenings")
                    SleepDetailItem(value = "11m", labelFirstLine = "Average", labelRest = "Time\nto fall asleep")
                }
            }
        }
    }
}