package mad.project.SleepMonitor.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import mad.project.SleepMonitor.navigation.Screen
import mad.project.SleepMonitor.R

@Composable
fun ProfileScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A1624)) // Тот же цвет фона, что и у MainScreen
    ) {
        // Основной контент с отступами сверху
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(top = 48.dp) // Большой отступ сверху
        ) {
            ProfileHeader() // Заголовок с именем и датой
            Spacer(modifier = Modifier.height(16.dp)) // Отступ между элементами
            ProfileDetails() // Секция с деталями профиля (например, текстовые данные)
        }

        // Подключаем BottomNavBar
        BottomNavBar(navController)
    }
}

@Composable
fun ProfileHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 48.dp) // Боковые отступы
    ) {
        Text(
            text = "Welcome,",
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
        Text(
            text = "Cristiano Ronaldo", // Здесь можно заменить на динамическое имя
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier.padding(top = 4.dp)
        )

        Row(
            modifier = Modifier
                .padding(top = 37.dp) // Отступ сверху
                .fillMaxWidth() // Для того, чтобы текст не выходил за пределы экрана
        ) {
            Text(
                text = "today is",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                modifier = Modifier.padding(end = 4.dp) // Отступ между текстами
            )
            Text(
                text = "22, march 2025", // Здесь можно динамически вставить текущую дату
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun ProfileDetails() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp) // Боковые отступы
    ) {
        // Можно добавить больше деталей профиля
        Text(
            text = "Profile Details:",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier.padding(top = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Age: 30", // Пример данных
            fontSize = 16.sp,
            color = Color(0xFFB8B8B8)
        )
        Text(
            text = "Email: cristiano@ronaldo.com", // Пример данных
            fontSize = 16.sp,
            color = Color(0xFFB8B8B8),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
@Composable
fun BottomNavBar(navController: NavController) {
    NavigationBar(
        containerColor = Color(0xFF152238),
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    "Home",
                    fontSize = 12.sp,
                    color = Color.White
                )
            },
            selected = false,
            onClick = {
                // Переход на MainScreen
                navController.navigate(Screen.MainScreen.route) {
                    // Переход на экран Main, но без повторного добавления в стек
                    popUpTo(Screen.MainScreen.route) { inclusive = true }
                }
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bar_chart),
                    contentDescription = "Abilities",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    "Abilities",
                    fontSize = 12.sp,
                    color = Color.White
                )
            },
            selected = false,
            onClick = {
                // Переход на экран "abilities"
                navController.navigate("abilities") {
                    // Это предотвращает накопление экранов в стеке
                    popUpTo("abilities") { inclusive = true }
                }
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    "Profile",
                    fontSize = 12.sp,
                    color = Color.White
                )
            },
            selected = true,
            onClick = {
                // Переход на экран профиля
                navController.navigate(Screen.ProfileScreen.route) {
                    // Добавляем флаг, чтобы не дублировать экран
                    popUpTo(Screen.ProfileScreen.route) { inclusive = true }
                }
            }
        )
    }
}
