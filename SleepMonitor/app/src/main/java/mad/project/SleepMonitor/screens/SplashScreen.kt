package mad.project.SleepMonitor.screens;

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import mad.project.SleepMonitor.R
import mad.project.SleepMonitor.navigation.Screen

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate(Screen.MainScreen.route) {
            popUpTo(Screen.SplashScreen.route) { inclusive = true }
        }
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF011222),
    ){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = "Sleep ",
                    color = Color.White,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold
                )
                Image(
                    painter = painterResource(id = R.drawable.sleep_line),
                    contentDescription = "Logo",
                    modifier = Modifier.size(30.dp)
                )

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreviewOfSplashScreen() {
    val navController = rememberNavController()
    SplashScreen(navController = navController)
}
