package mad.project.SleepMonitor.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun AppScaffold(
    navController: NavController,
    content: @Composable ColumnScope.() -> Unit
) {
    // Получаем текущий маршрут для определения активной вкладки
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A1624))
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(top = 48.dp)
        ) {

            content()
            Spacer(modifier = Modifier.height(16.dp))
        }

        BottomNavBar(navController = navController, currentRoute = currentRoute)
    }
}