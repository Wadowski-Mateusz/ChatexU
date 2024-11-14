package com.example.chatexu.presentation.add_friend.components


import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.presentation.add_friend.AddFriendViewModel

@Composable
fun RequestsLazyList(
    viewModel: AddFriendViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.state.value
    LazyColumn(
        modifier = modifier
    ) {
        Log.d("enter", "RequestsLazyList")
        items(items = state.users, key = {it.id}) {user ->
            if (state.outgoingRequests.keys.map { it.id } .contains(user.id))
                RequestedUserItem(
                    user = user,
                    onItemClick = {
                        Log.d("PEEK", "Undo user ${user.nickname}")
                        val requestToDelete = state.requests.first { it.recipientId == user.id }
                        viewModel.deleteFriendRequest(requestToDelete)
                    }
                )
            else if (state.incomingRequests.keys.map { it.id } .contains(user.id)) {
                IncomingRequestItem(
                    user = user,
                    onItemClick = {
//                        Log.d("PEEK", "Incoming request ${user.nickname}")
                        Log.d(DebugConstants.TODO, "Incoming request ${user.nickname}")
                    }

                )
            }
            else
                StrangerItem(
                    user = user,
                    onItemClick = {
                        Log.d("PEEK", "Add user ${user.nickname}")
                        viewModel.sendFriendRequest(user.id)
                    }
                )
        }


//        items(items = state.strangers, key = {it.id}) {stranger ->
//            FriendItem(
//                user = stranger,
//                onItemClick = {
//                    Log.d("PEEK", "Clicked stranger ${stranger.nickname}")
//                    viewModel.sendFriendRequest(stranger.id)
//                }
//            )
//        }
//
//        items(items = state.friends, key = {it.id}) {friend ->
//            FriendItem(
//                user = friend,
//                onItemClick = {
//                    Log.d("PEEK", "Clicked friend ${friend.nickname}")
//                }
//            )
//        }

    }
}
