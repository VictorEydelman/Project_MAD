package mad.project.SleepMonitor.ui.abilities

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
internal fun TimeRangeButton( // internal - виден внутри модуля
    text: String,
    timeRange: TimeRange,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) ButtonActiveBackground else ButtonInactiveBackground
    val textColor = if (isSelected) ButtonActiveText else ButtonInactiveText
    val buttonHeight = 36.dp
    val cornerRadius = 18.dp

    Surface(
        modifier = modifier
            .height(buttonHeight)
            .clip(RoundedCornerShape(cornerRadius))
            .clickable(onClick = onClick),
        color = backgroundColor,
        shape = RoundedCornerShape(cornerRadius)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = textColor,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}