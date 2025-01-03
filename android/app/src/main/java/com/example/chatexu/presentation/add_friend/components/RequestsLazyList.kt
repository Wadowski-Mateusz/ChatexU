package com.example.chatexu.presentation.add_friend.components


import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.domain.model.Friend
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

        val users = state.users

        val incomingRequestSenders: List<String> = state.incomingRequests.map { it.senderId }
        val outgoingRequestRecipients: List<String> = state.outgoingRequests.map { it.recipientId }

        val friendsIDs: List<String> = state.friends + state.acceptedRequests.map { it.senderId }

        val listOfIncoming: List<User> = users.filter { it.id in incomingRequestSenders }
        val listOfOutgoing: List<User> = users.filter { it.id in outgoingRequestRecipients }
        val listOfStrangers: List<User> = users.filter { it !in listOfIncoming && it !in listOfOutgoing }

        val sortedListOfUsersToShow: List<User> = listOfIncoming + listOfOutgoing + listOfStrangers

        items(items = sortedListOfUsersToShow, key = { it.id } ) { user ->
            if (user.id in outgoingRequestRecipients) {
                RequestedUserItem(
                    user = user,
                    onItemClick = {
//                        Log.d(DebugConstants.PEEK, "RequestsLazyList.RequestedUserItem() - " +
//                                "delete friend request to user ${user.nickname} from ${state.currentUserId}")
                        val requestToDelete = state.requests.first { it.recipientId == user.id }
                        viewModel.deleteFriendRequest(requestToDelete)
                    }
                )
            }
            else if (user.id in incomingRequestSenders) {
                IncomingRequestItem(
                    user = user,
                    onItemClickAccept = {
                        Log.d(DebugConstants.PEEK, "Accept incoming request from user ${user.nickname}")
                        val actionRequest = state.requests.first { it.senderId == user.id }
                        viewModel.acceptFriendRequest(actionRequest)
                    },
                    onItemClickReject = {
                        Log.d(DebugConstants.PEEK, "Reject incoming request from user ${user.nickname}")
                        val actionRequest = state.requests.first { it.senderId == user.id }
                        viewModel.rejectFriendRequest(actionRequest)
                    }
                )
            }
            else if (user.id == state.currentUserId) {} // Do not show current user
            else if (user.id !in friendsIDs){
//                Log.d(DebugConstants.PEEK, user.nickname + "as default")
                // this if may broke something in the future
                // implemented so after accepting friend request, request diapers from list of request
                val requestSenders = state.acceptedRequests.map { it.senderId }.toSet()
                if(!requestSenders.contains(user.id))
                    StrangerItem(
                        user = user,
                        onItemClick = {
                            Log.d(DebugConstants.PEEK, "Add user ${user.nickname}")
                            viewModel.sendFriendRequest(user.id)
                        }
                    )
            } else if (user.id in friendsIDs) {
                FriendItem(
                    user = user,
                    onItemClick = { Log.d(DebugConstants.TODO, "Friend has been clicked - delete friend") }
                )
            } else {
                Log.w(DebugConstants.POTENTIAL_BUG, "RequestsLazyList - user ${user.id} : ${user.nickname} not assigned to any item to display")
            }
        }

    }
}
