package mad.project.SleepMonitor.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import mad.project.SleepMonitor.ui.common.AppScaffold

@Composable
fun ProfileScreen(navController: NavController) {
    AppScaffold(navController = navController) {
        ProfileHeader()
        Spacer(modifier = Modifier.height(16.dp))
        ProfileDetails()
        Spacer(modifier = Modifier.height(32.dp))
    }
}


@Composable
fun ProfileHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 48.dp)
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
                .padding(top = 37.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "today is",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                modifier = Modifier.padding(end = 4.dp)
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
fun ProfileDetails() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp) // Сохраняем отступы для контента
    ) {
        Text(
            text = "Profile Details:",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier.padding(top = 16.dp) // Отступ внутри секции деталей
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Age: 30",
            fontSize = 16.sp,
            color = Color(0xFFB8B8B8)
        )
        Text(
            text = "Email: cristiano@ronaldo.com",
            fontSize = 16.sp,
            color = Color(0xFFB8B8B8),
            modifier = Modifier.padding(top = 4.dp)
        )

    }
}
