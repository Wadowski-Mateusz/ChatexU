package com.example.chatexu.presentation.auth


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatexu.common.Constants
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.presentation.Screen
import com.example.chatexu.presentation.auth.components.FastButton
import com.example.chatexu.presentation.commons.composable.ScreenName

@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val login = remember { mutableStateOf(TextFieldValue()) }
    val password = remember { mutableStateOf(TextFieldValue()) }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        ScreenName(screenName = "Auth")


        if(state.error.isNotBlank()) {
            Text(
                text = "Error when connecting to the database",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Yellow)
                    .padding(vertical = 16.dp),
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        if(state.loginPage) {

            if(state.badLoginData)
                Row(modifier = Modifier.background(Color.Red)) {
                    Text(text = "BAD DATA")
                }

            TextField(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 8.dp)
                    .align(Alignment.CenterHorizontally),
                value = login.value,
                onValueChange = { login.value = it },
                placeholder = { Text(text = "Login") },
                maxLines = 1
            )

            TextField(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 8.dp)
                    .align(Alignment.CenterHorizontally),
                value = password.value,
                onValueChange = { password.value = it },
                placeholder = { Text(text = "********") },
                maxLines = 1
            )

            FastButton(
                txt = "Login",
                onClick = {
                    Log.d("PEEK", "CLICK")
                    viewModel.login(login.value.text, password.value.text)
                    Log.d("PEEK", "RESPONSE: ${viewModel.state.value.userId}")

                    if(state.error.isBlank() && state.userId != Constants.ID_DEFAULT) {
                        viewModel.setBadLoginDataAlertVisibility(false)
                        navController.navigate(Screen.ChatListScreen.route  + "/${state.userId}")
                    } else {
                        viewModel.setBadLoginDataAlertVisibility(true)
                    }

                }
            )

            FastButton(
                txt = "Register",
                onClick = {
                    viewModel.showRegister()
                }
            )

            FastButton(
                txt = "GO TO DEBUG",
                onClick = {
                    Log.d("PEEK", "CLICK")
                    navController.navigate(Screen.AuthScreenDebug.route)
                }
            )


        }

        if(state.registerPage) {

            val registerEmail = remember { mutableStateOf(TextFieldValue()) }
            val registerNickname = remember { mutableStateOf(TextFieldValue()) }
            val registerPassword = remember { mutableStateOf(TextFieldValue()) }
            val registerPasswordRepeat = remember { mutableStateOf(TextFieldValue()) }

            TextField(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 8.dp)
                    .align(Alignment.CenterHorizontally),
                value = registerEmail.value,
                onValueChange = { registerEmail.value = it },
                placeholder = { Text(text = "email") },
                maxLines = 1
            )

            TextField(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 8.dp)
                    .align(Alignment.CenterHorizontally),
                value = registerNickname.value,
                onValueChange = { registerNickname.value = it },
                placeholder = { Text(text = "nickname") },
                maxLines = 1
            )

            TextField(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 8.dp)
                    .align(Alignment.CenterHorizontally),
                value = registerPassword.value,
                onValueChange = { registerPassword.value = it },
                placeholder = { Text(text = "pass") },
                maxLines = 1
            )

            TextField(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 8.dp)
                    .align(Alignment.CenterHorizontally),
                value = registerPasswordRepeat.value,
                onValueChange = { registerPasswordRepeat.value = it },
                placeholder = { Text(text = "repeat pass") },
                maxLines = 1
            )

            FastButton(
                txt = "Register",
                onClick = {
                    if(registerPassword.value.text == registerPasswordRepeat.value.text) {

                        viewModel.register(
                            registerEmail.value.text,
                            registerNickname.value.text,
                            registerPassword.value.text
                        )
                        viewModel.showLogin()
                        navController.navigate(Screen.ChatListScreen.route  + "/${state.userId}")
                    } else {
                        Log.d(DebugConstants.PEEK, "Passwords are not the same")
                    }
                }
            )

            FastButton(
                txt = "Back to login",
                onClick = {
                    viewModel.showLogin()
                }
            )
        }



    }
}

