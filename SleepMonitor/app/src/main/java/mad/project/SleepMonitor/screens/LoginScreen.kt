package mad.project.SleepMonitor.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mad.project.SleepMonitor.components.*
import mad.project.SleepMonitor.navigation.Screen
import mad.project.SleepMonitor.viewmodels.AuthViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    Surface(
        color = Color(0xFF011222),
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF011222))
            .padding(40.dp, 70.dp, 40.dp, 35.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                TitleTextComponent(value = "Login")
                NormalTextComponent(value = "Welcome, we’re glad you’re back!")
                Spacer(modifier = Modifier.height(110.dp))
                NormalTextComponent(value = "Username")
                TextFieldComponent(
                    text = username,
                    onTextChanged = { username = it }
                )
                Spacer(modifier = Modifier.height(24.dp))
                NormalTextComponent(value = "Password")
                PasswordFieldComponent(
                    password = password,
                    onPasswordChanged = { password = it }
                )
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                ) {
                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = Color.Red,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
                ButtonComponent(
                    value = "LOGIN",
                    onClick = {
                        if (username.isBlank() || password.isBlank()) {
                            errorMessage = "Please enter username and password"
                            return@ButtonComponent
                        }
                        isLoading = true
                        errorMessage = ""
                        viewModel.login(username, password) { success, message ->
                            isLoading = false
                            if (success) {
                                navController.navigate(Screen.SplashScreen.route) {
                                    popUpTo(Screen.LoginScreen.route) { inclusive = true }
                                }
                            }
                        }
                    }
                )
            }
            ClickableLoginTextComponent(
                initialText = "DON’T HAVE AN ACCOUNT? ",
                clickableText = "SIGN UP",
                tag = "register",
                onTextSelected = { tag ->
                    if (tag == "register") {
                        navController.navigate(Screen.SignUpScreen.route)
                    }
                }
            )
        }
    }
}