package mad.project.SleepMonitor.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
//import dto.AuthRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mad.project.SleepMonitor.components.*
import mad.project.SleepMonitor.navigation.Screen

@Composable

fun LoginScreen(navController: NavController) {
    Surface(
        color = Color(0xFF011222),
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF011222))
            .padding(40.dp, 70.dp, 40.dp, 35.dp)
    ) {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var isLoading by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf("") }

        Column(modifier = Modifier.fillMaxSize()) {
            TitleTextComponent(value = "Login")
            NormalTextComponent(value = "Welcome we’re glad you’re back")

            Spacer(modifier = Modifier.height(110.dp))
            NormalTextComponent(value = "Username")
            TextFieldComponent()
            Spacer(modifier = Modifier.height(24.dp))
            NormalTextComponent(value = "Password")
            PasswordFieldComponent()

            Spacer(modifier = Modifier.height(50.dp))
            ButtonComponent(
                value = "LOGIN",
                onClick = {
                    isLoading = true
                    errorMessage = ""
//                    val authRequest = AuthRequest(username, password)
                    CoroutineScope(Dispatchers.Main).launch {
                        try{
//                            val response = service.AuthService.login(authRequest)
                            navController.navigate(Screen.SplashScreen.route) {
                                popUpTo(Screen.LoginScreen.route) { inclusive = true }
                            }
                        } catch (e: Exception) {
                            errorMessage = "Login failed: ${e.message}"
                        } finally {
                            isLoading = false
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(25.dp))
            ClickableLoginTextComponent(
                initialText = "DON’T HAVE AN ACCOUNT? ",
                clickableText = "SIGN UP",
                tag = "register",
                onTextSelected = { tag ->
                    if (tag == "register") { navController.navigate(Screen.SignUpScreen.route) }
                }
            )
        }
    }
}
