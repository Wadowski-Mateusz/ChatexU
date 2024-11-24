@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.example.chatexu.presentation.add_friend

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
import com.example.chatexu.presentation.add_friend.components.RequestsLazyList
import com.example.chatexu.presentation.commons.composable.ScreenName

@Composable
fun AddFriendScreen(
    navController: NavController,
    viewModel: AddFriendViewModel = hiltViewModel(),
) {
    val state = viewModel.state.value
    val phrase = remember { mutableStateOf("") }
    val isSearchActive = remember { mutableStateOf(false) }

    val search = {
        viewModel.filterUsersByNickname(phrase.value)
        isSearchActive.value = false
    }


    Column {
        ScreenName(screenName = "ADD FRIEND")

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
                .padding(horizontal = 16.dp, vertical = 8.dp),
            query = phrase.value,
            onQueryChange = { phrase.value = it },
            onSearch = { search() },
            active = isSearchActive.value,
            onActiveChange = { isSearchActive.value = it },
            placeholder = { Text(text = "User nickname") },
            leadingIcon = {
                Icon(
                    modifier = Modifier.clickable { search() },
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search icon"
                )
                          },
            trailingIcon = {
                if (isSearchActive.value) {
                    Icon(
                        modifier = Modifier.clickable {
                            if (phrase.value.isEmpty()) {
                                isSearchActive.value = false
                                viewModel.reload()
                            }
                            phrase.value = ""
                        },
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear icon"
                    )
                }
            },
        ) {}

        RequestsLazyList(
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModel,
        )

    }

}