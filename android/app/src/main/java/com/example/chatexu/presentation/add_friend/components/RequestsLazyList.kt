package com.example.chatexu.presentation.add_friend.components


import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.domain.model.User
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

        val users = state.users

        val listOfIncoming: List<User> = users.filter { it.id in state.incomingRequests.keys }
        val listOfOutgoing: List<User> = users.filter { it.id in state.outgoingRequests.keys }
        val listOfStrangers: List<User> = users.filter { it !in listOfIncoming && it !in listOfOutgoing }

        val sortedListOfUsersToShow: List<User> = listOfIncoming + listOfOutgoing + listOfStrangers

        items(items = sortedListOfUsersToShow, key = { it.id } ) { user ->

            if (state.outgoingRequests.keys.contains(user.id)) {
//                Log.d(DebugConstants.PEEK, user.nickname + "as outgoing")
                RequestedUserItem(
                    user = user,
                    onItemClick = {
                        Log.d(DebugConstants.PEEK, "RequestsLazyList.RequestedUserItem() - " +
                                "delete friend request to user ${user.nickname} from ${state.currentUserId}")
                        val requestToDelete = state.requests.first { it.recipientId == user.id }
                        viewModel.deleteFriendRequest(requestToDelete)
                    }
                )
            }
            else if (state.incomingRequests.keys.map { it } .contains(user.id)) {
//                Log.d(DebugConstants.PEEK, user.nickname + "as incoming")
                IncomingRequestItem(
                    user = user,
                    onItemClickAccept = {
                        Log.d(DebugConstants.PEEK, "Accept incoming request from user ${user.nickname}")

                        val actionRequest = state.requests.first { it.senderId == user.id }
                        viewModel.acceptFriendRequest(actionRequest)
                        // TODO
                    },
                    onItemClickReject = {
                        Log.d(DebugConstants.PEEK, "Reject incoming request from user ${user.nickname}")
                        val actionRequest = state.requests.first { it.senderId == user.id }
                        viewModel.rejectFriendRequest(actionRequest)
                        // TODO
                    }
                )
            }
            else {
//                Log.d(DebugConstants.PEEK, user.nickname + "as default")
                // this if may broke something in the future
                // implemented so after accepting friend request, request diapers from list of request
                val requestSenders = state.acceptedRequests.map { it.senderId }.toSet()
                if(!requestSenders.contains(user.id))
                    StrangerItem(
                        user = user,
                        onItemClick = {
                            Log.d("PEEK", "Add user ${user.nickname}")
                            viewModel.sendFriendRequest(user.id)
                        }
                    )
            }
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
