package com.example.chatexu.presentation.auth.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatexu.domain.model.User


@Composable
fun UserItem(
    user: User,
    onItemClick: (User) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(user) }
            .padding(16.dp)
    ) {
        //Text(text = "id: ${user.id.takeLast(4)}", modifier = Modifier.padding(8.dp))
        Text(text = /*"nickname: */"${user.nickname}", modifier = Modifier.padding(8.dp))
    }
}

@Preview
@Composable
fun Preview() {
    val u = User("0123456789", "nickname user", username = "USER USERNAME")
    UserItem(user = u, onItemClick = {})
}