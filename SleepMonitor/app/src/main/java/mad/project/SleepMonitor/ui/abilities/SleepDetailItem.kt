package mad.project.SleepMonitor.ui.abilities

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import mad.project.SleepMonitor.ui.theme.White


@Composable
internal fun RowScope.SleepDetailItem(
    value: String,
    labelFirstLine: String,
    labelRest: String
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(end = 4.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = value,
            color = White,
            fontSize = 26.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = LabelColor, fontSize = 13.sp, fontWeight = FontWeight.Normal)) {
                    append(labelFirstLine)
                }
                if (labelRest.isNotEmpty()) {
                    append("\n")
                }
                withStyle(style = SpanStyle(color = White, fontSize = 13.sp, fontWeight = FontWeight.Normal)) {
                    append(labelRest)
                }
            },
            textAlign = TextAlign.Start,
            lineHeight = 16.sp
        )
    }
}