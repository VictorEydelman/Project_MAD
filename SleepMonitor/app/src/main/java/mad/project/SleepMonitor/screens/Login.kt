package com.example.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.components.*
import mad.project.SleepMonitor.navigation.Screen

@Composable
fun LoginScreen(navController: NavController){
    Surface(
        color = Color(0xFF011222),
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF011222))
            .padding(40.dp, 70.dp, 40.dp, 35.dp)
    ){
        Column(modifier = Modifier.fillMaxSize()) {
            TitleTextComponent(value = "Login")
            NormalTextComponent(value = "Welcome we’re glad you’re back")

            Spacer(modifier = Modifier.height(110.dp))
            NormalTextComponent(value = "Username")
            TextFieldComponent()
            Spacer(modifier = Modifier.height(24.dp))
            NormalTextComponent(value = "Password")
            PasswordFieldComponent()

            Spacer(modifier = Modifier.height(80.dp))
            ButtonComponent("LOGIN")
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

@Preview
@Composable
fun DefaultPreviewOfLoginScreen(){
    val navController = rememberNavController()
    LoginScreen(navController = navController)
}
