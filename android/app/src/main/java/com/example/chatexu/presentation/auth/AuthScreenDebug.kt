package com.example.chatexu.presentation.auth


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatexu.presentation.Screen
import com.example.chatexu.presentation.auth.components.FastButton
import com.example.chatexu.presentation.auth.components.UserItem
import com.example.chatexu.presentation.commons.composable.ScreenName

@Composable
fun AuthScreenDebug(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        ScreenName(screenName = "Auth DEBUG")


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

//        if(state.users.isEmpty())
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color(red = 10, green = 200, blue = 50, alpha = 0)),
                onClick = { viewModel.create() }
            ) {
                Text("Click to create users and chat")
            }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(red = 0.9f, green = 1f, blue = 1f))
        ) {
            items(items = state.users, key = {it.id}) { user ->
                UserItem (
                    user = user,
                    onItemClick = {
                        navController.navigate(Screen.ChatListScreen.route  + "/${user.id}")
                    }
                )
                Divider(color = Color.Black, thickness = 1.dp)
            }
        }

        FastButton(
            txt = "GO TO LOGIN",
            onClick = {
                Log.d("PEEK", "CLICK")
                navController.navigate(Screen.AuthScreen.route)
            }
        )


    }
}

