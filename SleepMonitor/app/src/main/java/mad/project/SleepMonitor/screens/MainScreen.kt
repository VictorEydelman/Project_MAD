package mad.project.SleepMonitor.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import mad.project.SleepMonitor.R  // Убедитесь, что импортируется правильный R файл

@Composable
fun MainScreen(navController: NavController) {
    // Состояние для отслеживания активной вкладки
    val selectedTab = remember { mutableStateOf("main") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A1624))
    ) {
        // Основной контент с отступами сверху
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(top = 48.dp) // Большой отступ сверху
        ) {
            Header()
            Spacer(modifier = Modifier.height(16.dp)) // Уменьшенный отступ между Header и картинкой
            ImageSection() // Картинка на всю ширину
            Spacer(modifier = Modifier.height(16.dp)) // Отступ перед блоками
            TimeCardsGrid() // Блоки с карточками
            Spacer(modifier = Modifier.height(32.dp)) // Увеличенный отступ

            Spacer(modifier = Modifier.height(32.dp)) // Увеличенный отступ
        }

        // Навигационная панель
        BottomNavBar(navController = navController, selectedTab = selectedTab)
    }
}

@Composable
fun BottomNavBar(navController: NavController, selectedTab: MutableState<String>) {
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
                    "home",
                    fontSize = 12.sp,
                    color = Color.White
                )
            },
            selected = selectedTab.value == "main",  // Проверяем, если это текущий экран
            onClick = {
                selectedTab.value = "main"
                navController.navigate("main") {
                    popUpTo("main") { inclusive = true } // При переходе удаляем все экраны из стека
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
                    "abilities",
                    fontSize = 12.sp,
                    color = Color.White
                )
            },
            selected = selectedTab.value == "abilities", // Проверка для активной вкладки
            onClick = {
                selectedTab.value = "abilities"
                navController.navigate("abilities")
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
                    "profile",
                    fontSize = 12.sp,
                    color = Color.White
                )
            },
            selected = selectedTab.value == "profile",  // Проверка для активной вкладки
            onClick = {
                selectedTab.value = "profile"
                navController.navigate("profile_screen")
            }
        )
    }
}
@Composable
fun Header() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 48.dp) // Добавлены боковые отступы
    ) {
        Text(
            text = "Welcome,",
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
        Text(
            text = "Cristiano Ronaldo",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier.padding(top = 4.dp)
        )

        Row(
            modifier = Modifier
                .padding(top = 37.dp) // Уменьшенный отступ сверху
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
                text = "22, march 2025",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
        }
    }
}
@Composable
fun ImageSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Размер изображения
            .padding(horizontal = 24.dp) // Боковые отступы
    ) {
        Image(
            painter = painterResource(id = R.drawable.sleep_illustration), // Замените на ваше изображение
            contentDescription = "Sleep illustration",
            modifier = Modifier.fillMaxSize()
        )
    }
}
@Composable
fun TimeCardsGrid() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp) // Боковые отступы
    ) {
        // Первая строка карточек
        Row(modifier = Modifier.fillMaxWidth()) {
            TimeCard(
                title = "Alarm",
                time = "04 : 00", // Пробелы вокруг двоеточия
                iconRes = R.drawable.ic_alarm,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            TimeCard(
                title = "Bedtime",
                time = "22 : 00", // Пробелы вокруг двоеточия
                iconRes = R.drawable.ic_bedtime,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Вторая строка карточек
        Row(modifier = Modifier.fillMaxWidth()) {
            TimeCard(
                title = "Recommended wake up",
                time = "06 : 00", // Пробелы вокруг двоеточия
                iconRes = R.drawable.ic_wbsunny,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            TimeCard(
                title = "Recommended bedtime",
                time = "22 : 00", // Пробелы вокруг двоеточия
                iconRes = R.drawable.ic_bedtime,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun TimeCard(
    title: String,
    time: String,
    @DrawableRes iconRes: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF152238))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start, // Выравниваем всё по левому краю
            verticalArrangement = Arrangement.Center
        ) {
            // Используем Row, чтобы иконка и текст шли рядом
            Row(
                verticalAlignment = Alignment.CenterVertically, // Для выравнивания иконки и текста по центру по вертикали
                horizontalArrangement = Arrangement.Start // Выравниваем всё по левому краю
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = title,
                    modifier = Modifier.size(24.dp) // Уменьшаем размер иконки
                )
                Spacer(modifier = Modifier.width(8.dp)) // Отступ между иконкой и текстом
                Text(
                    text = title,
                    color = Color(0xFF8E8E93),
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = time,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 22.sp
            )
        }
    }
}