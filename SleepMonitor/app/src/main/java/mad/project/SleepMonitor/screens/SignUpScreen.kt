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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import mad.project.SleepMonitor.components.*
import mad.project.SleepMonitor.navigation.Screen
import mad.project.SleepMonitor.viewmodels.AuthViewModel

@Composable
fun SignUpScreen(navController: NavController, viewModel: AuthViewModel) {
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
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ){
            Column{
                TitleTextComponent(value = "Sign Up")
                NormalTextComponent(value = "Let’s get started to better your sleep")
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
                    value = "SIGN UP",
                    onClick = {
                        if (username.isBlank() || password.isBlank()) {
                            errorMessage = "Please enter username and password"
                            return@ButtonComponent
                        }
                        isLoading = true
                        errorMessage = ""
                        viewModel.register(username, password) { success, message ->
                            isLoading = false
                            errorMessage = message
                            if (success) {
                                navController.navigate(Screen.LoginScreen.route) {
                                    popUpTo(Screen.SignUpScreen.route) { inclusive = true }
                                }
                            }
                        }
                    }
                )
            }
            ClickableLoginTextComponent(
                initialText = "ALREADY HAVE AN ACCOUNT? ",
                clickableText = "LOG IN",
                tag = "login",
                onTextSelected = { tag ->
                    if (tag == "login") {
                        navController.navigate(Screen.LoginScreen.route)
                    }
                }
            )
        }
    }
}