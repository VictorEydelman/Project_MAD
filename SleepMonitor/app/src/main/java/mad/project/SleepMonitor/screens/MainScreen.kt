package mad.project.SleepMonitor.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
@Composable
fun MainScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF011222) // Используем тот же цвет фона
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF011222)), // Фон для Box на всякий случай
            contentAlignment = Alignment.Center // Центрируем содержимое
        ) {
            Text(
                text = "Main Screen",
                color = Color.White, // Белый текст
                fontSize = 24.sp, // Размер шрифта
                fontWeight = FontWeight.Bold // Жирный шрифт
            )
            // Сюда позже можно будет добавить реальное содержимое главного экрана
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreviewOfMainScreen() {
    // Для превью нужен NavController, создаем заглушку
    val navController = rememberNavController()
    MainScreen(navController = navController)
}