@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package com.example.chatexu.presentation.create_chat

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatexu.presentation.commons.composable.ScreenName
import com.example.chatexu.presentation.create_chat.components.FriendLazyList

@Composable
fun CreateChatScreen(
    navController: NavController,
    viewModel: CreateChatViewModel = hiltViewModel(),
) {
    val state = viewModel.state.value
    val phrase = remember { mutableStateOf("") }
//    val inputMessage = remember { mutableStateOf(TextFieldValue()) }
    val active = remember { mutableStateOf(false) }


    fun search() {
        viewModel.filterFriendsByNickname(phrase.value)
        active.value = false
    }


    Column {
//        ScreenName(screenName = "CREATE CHAT")

        if(state.error.isNotBlank()) {
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
        }

        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(1.dp),
            query = phrase.value,
            onQueryChange = { phrase.value = it },
            onSearch = { search() },
            active = active.value,
            onActiveChange = { active.value = it },
            placeholder = { Text(text = "Friend nickname") },
            leadingIcon = {
                Icon(
                    modifier = Modifier.clickable { search() },
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search icon"
                )
                          },
            trailingIcon = {
                if (active.value) {
                    Icon(
                        modifier = Modifier.clickable {
                            if (phrase.value.isEmpty()) {
                                Log.d("PRRK", "EMPTY")
                                active.value = false
                            }
                            phrase.value = ""
                        },
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear icon"
                    )
                }
                           },
        ) {}

        FriendLazyList(
            friends = state.matchingFriends,
            navController = navController,
            modifier = Modifier.fillMaxSize(),
            userId = state.userId,
            getChatOrElseCreate = viewModel::getChatId
        )


    }

}