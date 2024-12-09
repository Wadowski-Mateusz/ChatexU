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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.chatexu.presentation.auth.components.PasswordInput
import com.example.chatexu.presentation.commons.composable.ScreenName

@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val login = remember { mutableStateOf(TextFieldValue()) }
    val password = remember { mutableStateOf(TextFieldValue()) }
    val loginMaxLength = 16
    val passwordMaxLength = 24

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        ScreenName(screenName = "Auth")

        val fieldModifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 16.dp)
            .align(Alignment.CenterHorizontally)

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

            if(state.badLoginData) {
                Row(modifier = Modifier.background(Color.Red)) {
                    Text(text = "BAD DATA")
                }
            }

            // Login - user login
            TextField(
                modifier = fieldModifier,
                value = login.value,
                onValueChange = { login.value = viewModel.trimInput(it, loginMaxLength) },
                placeholder = { Text(text = "login") },
                label = { Text(text = "Login") },
                singleLine = true,
            )


            PasswordInput(
                password = password,
                modifier = fieldModifier,
                trimInput = viewModel::trimInput,
                passwordMaxLength = passwordMaxLength
            )

            FastButton(
                txt = "Login",
                onClick = {
                    viewModel.login(login.value.text, password.value.text)
                    Log.d(DebugConstants.PEEK, "RESPONSE: ${viewModel.state.value.userId}")

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
                modifier = fieldModifier,
                value = registerEmail.value,
                onValueChange = { registerEmail.value = it },
                placeholder = { Text(text = "email@example.com") },
                label = { Text(text = "email") },
                singleLine = true,
            )

            TextField(
                modifier = fieldModifier,
                value = registerNickname.value,
                onValueChange = { registerNickname.value = viewModel.trimInput(it, loginMaxLength) },
                placeholder = { Text(text = "nickname") },
                label = { Text(text = "Nickname") },
                singleLine = true,
            )

            PasswordInput(
                password = registerPassword,
                modifier = fieldModifier,
                trimInput = viewModel::trimInput,
                passwordMaxLength = passwordMaxLength,
            )

            PasswordInput(
                password = registerPasswordRepeat,
                modifier = fieldModifier,
                trimInput = viewModel::trimInput,
                passwordMaxLength = passwordMaxLength,
            )

            FastButton(
                txt = "Register",
                onClick = {
                    if(registerPassword.value.text == registerPasswordRepeat.value.text) {
                        viewModel.registerUser(
                            registerEmail.value.text,
                            registerNickname.value.text,
                            registerPassword.value.text
                        )
                    } else {
                        Log.e(DebugConstants.PEEK, "Passwords are not the same")
                    }
                }
            )

            FastButton(
                txt = "Go back to login",
                onClick = {
                    viewModel.showLogin()
                }
            )
        }

        LaunchedEffect(state) {
            if (!state.isLoading && state.error.isBlank() && state.registerStatus) {
                Log.i("REGISTER - SCREEN - true", state.userId)
                navController.navigate(Screen.ChatListScreen.route + "/${state.userId}")
            } else if (!state.isLoading && state.error.isBlank() && state.loginStatus) {
                Log.i("LOGIN - SCREEN - true", state.userId)
                navController.navigate(Screen.ChatListScreen.route + "/${state.userId}")
//            } else if (!state.isLoading && state.error.isNotBlank() ) {
//                viewModel.showRegister()
//            }
            }

        }





    }
}

