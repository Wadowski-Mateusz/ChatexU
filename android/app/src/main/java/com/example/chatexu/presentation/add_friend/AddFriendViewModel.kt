package com.example.chatexu.presentation.add_friend

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatexu.common.Constants
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.domain.model.Friend
import com.example.chatexu.domain.model.FriendRequest
import com.example.chatexu.domain.model.User
import com.example.chatexu.domain.use_case.debug.get_all_users.GetAllUsersUseCase
import com.example.chatexu.domain.use_case.delete_friend_request_use_case.DeleteFriendRequestUseCase
import com.example.chatexu.domain.use_case.delete_friend_request_use_case.DeleteRejectFriendRequestUseCase
import com.example.chatexu.domain.use_case.get_user_friend_requests_use_case.GetUserFriendRequestsUseCase
import com.example.chatexu.domain.use_case.get_user_friends.GetUserFriendsUseCase
import com.example.chatexu.domain.use_case.get_users_by_part_of_nickname_use_case.GetUsersByPartOfNicknameUseCase
import com.example.chatexu.domain.use_case.post_accept_friend_request_use_case.AcceptFriendRequestUseCase
import com.example.chatexu.domain.use_case.post_friend_request_use_case.PostFriendRequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AddFriendViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val deleteFriendRequestUseCase: DeleteFriendRequestUseCase,
    private val postFriendRequestUseCase: PostFriendRequestUseCase,
    private val deleteRejectFriendRequestUseCase: DeleteRejectFriendRequestUseCase,
    private val postAcceptFriendRequestUseCase: AcceptFriendRequestUseCase,
    private val getUserFriendRequestsUseCase: GetUserFriendRequestsUseCase,
    private val getUserFriendsUseCase: GetUserFriendsUseCase,
    private val getUsersByPartOfNicknameUseCase: GetUsersByPartOfNicknameUseCase,
    ): ViewModel() {

    private val _state = mutableStateOf<AddFriendState>(AddFriendState())
    val state: State<AddFriendState> = _state

    init {
        _state.value = _state.value.copy(
            currentUserId = savedStateHandle.get<String>(Constants.PARAM_USER_ID)
                ?: Constants.ID_DEFAULT // TODO what if null
        )

        reload()

    }

    fun reload() {
        loadSuggestedUsers()
        loadFriendRequest()
        loadFriends()

        groupUsers()
    }

    fun filterUsersByNickname(partOfNickname: String) {


        val trimmedPartOfNickname = partOfNickname.trim()

        if(trimmedPartOfNickname.isNotEmpty())
            fetchAndGroupUsersByPhrase(trimmedPartOfNickname)
        else
            reload()

    }

    private fun fetchAndGroupUsersByPhrase(partOfNickname: String) {

        val users = getUsersByPartOfNicknameUseCase(_state.value.currentUserId, partOfNickname)

        users.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    Log.d(DebugConstants.PEEK, "AddFriendViewModel.fetchAndGroupUsersByPhrase() - " +
                            "enter success fetchUsersByPhrase, ${result.data!!.size}")
                    _state.value = _state.value.copy(
                        users = result.data,
                        isLoading = false,
                        error = ""
                    )
                    groupUsers()
                }
                is DataWrapper.Loading -> {
                    Log.i(DebugConstants.RESOURCE_LOADING, "AddFriendViewModel.fetchAndGroupUsersByPhrase() - Loading")
                    _state.value = state.value.copy(
                        isLoading = true,
                        error = ""
                    )
                }
                is DataWrapper.Error -> {
                    Log.d(DebugConstants.PEEK, "AddFriendViewModel.fetchAndGroupUsersByPhrase() - Error ")
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message ?: "Unknown error"
                    )
                }
            }
        } .launchIn(viewModelScope)
    }

    fun sendFriendRequest(userToAddId: String)  {
        Log.d(DebugConstants.PEEK, "AddFriendViewModel.sendFriendRequest() - send request to user $userToAddId")

        val request = postFriendRequestUseCase(state.value.currentUserId, userToAddId)

        request.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    Log.d(DebugConstants.PEEK, "AddFriendViewModel.sendFriendRequest() - enter success, ${result.data}")

                    val friendRequestFromResponse: FriendRequest = result.data!!

                    _state.value = _state.value.copy(
                        requests = _state.value.requests.plus(friendRequestFromResponse),
                        outgoingRequests = _state.value.outgoingRequests.plus(friendRequestFromResponse),
                        isLoading = false,
                        error = ""
                    )

                }
                is DataWrapper.Loading -> {
                    Log.i(DebugConstants.RESOURCE_LOADING, "AddFriendViewModel.sendFriendRequest() - Loading")
                    _state.value = state.value.copy(
                        isLoading = true,
                        error = ""
                    )
                }
                is DataWrapper.Error -> {
                    Log.e(DebugConstants.PEEK, "AddFriendViewModel.sendFriendRequest() - Error")
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message ?: "Unknown error"
                    )
                }
            }
        } .launchIn(viewModelScope)

    }

    fun deleteFriendRequest(friendRequest: FriendRequest) {
        Log.d(DebugConstants.PEEK, "AddFriendViewModel.deleteFriendRequest() - delete request ${friendRequest.requestId}")

        val requestId = friendRequest.requestId
        val request = deleteFriendRequestUseCase(requestId)

        request.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    Log.d(DebugConstants.PEEK, "AddFriendViewModel.deleteFriendRequest() - Enter success deleteFriendRequest, ${result.data}")
                    _state.value = _state.value.copy(
                        requests = _state.value.requests.filter { it.requestId != requestId },
//                        outgoingRequests = _state.value.outgoingRequests.filter { it.value.requestId != requestId },
                        outgoingRequests = _state.value.outgoingRequests.filter { it -> it.requestId != requestId }.toSet(),
                        isLoading = false,
                        error = ""
                    )
                }
                is DataWrapper.Loading -> {
                    Log.i(DebugConstants.RESOURCE_LOADING, "AddFriendViewModel.deleteFriendRequest() - Loading")
                    _state.value = state.value.copy(
                        isLoading = true,
                        error = ""
                    )
                }
                is DataWrapper.Error -> {
                    Log.e(DebugConstants.PEEK, "AddFriendViewModel.deleteFriendRequest() - Error")
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message ?: "Unknown error"
                    )
                }
            }
        } .launchIn(viewModelScope)

    }

    fun acceptFriendRequest(friendRequest: FriendRequest) {
//        Log.d(DebugConstants.PEEK, "AddFriendViewModel.acceptFriendRequest()")

        val response = postAcceptFriendRequestUseCase(friendRequest.requestId)

        response.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    Log.d(DebugConstants.PEEK, "AddFriendViewModel.acceptFriendRequest() - Enter success acceptFriendRequest, ${result.data}")
                    _state.value = _state.value.copy(
                        incomingRequests = _state.value.incomingRequests.filter { it != friendRequest }.toSet(),
                        acceptedRequests = _state.value.acceptedRequests.plus(friendRequest),
                        friends = _state.value.friends.plus(friendRequest.senderId),
                        isLoading = false,
                        error = ""
                    )
                }
                is DataWrapper.Loading -> {
                    Log.i(DebugConstants.RESOURCE_LOADING, "AddFriendViewModel.acceptFriendRequest() - Loading")
                    _state.value = state.value.copy(
                        isLoading = true,
                        error = ""
                    )
                }
                is DataWrapper.Error -> {
                    Log.e(DebugConstants.PEEK, "AddFriendViewModel.acceptFriendRequest() - Error")
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message ?: "Unknown error"
                    )
                }
            }
        } .launchIn(viewModelScope)

    }


    fun rejectFriendRequest(friendRequest: FriendRequest) {
//        Log.w(DebugConstants.IN_PROGRESS, "AddFriendViewModel.rejectFriendRequest()")

        val response = deleteRejectFriendRequestUseCase(friendRequest.requestId)

        response.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    Log.d(DebugConstants.PEEK, "AddFriendViewModel.rejectFriendRequest() - Enter success acceptFriendRequest, ${result.data}")
                    _state.value = _state.value.copy(
                        requests = _state.value.requests.filter { it != friendRequest },
                        incomingRequests = _state.value.incomingRequests.filter { it != friendRequest }.toSet(),
                        isLoading = false,
                        error = ""
                    )
                }
                is DataWrapper.Loading -> {
                    Log.i(DebugConstants.RESOURCE_LOADING, "AddFriendViewModel.rejectFriendRequest() - Loading")
                    _state.value = state.value.copy(
                        isLoading = true,
                        error = ""
                    )
                }
                is DataWrapper.Error -> {
                    Log.e(DebugConstants.PEEK, "AddFriendViewModel.rejectFriendRequest() - Error")
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message ?: "Unknown error"
                    )
                }
            }
        } .launchIn(viewModelScope)


    }


    private fun loadSuggestedUsers() {
        Log.d("loadSuggestedUsers", "ENTER loadSuggestedUsers")
        val userId: String = _state.value.currentUserId
//        Log.d("loadSuggestedUsers", "1: $userId")
        val users = getAllUsersUseCase()
//        Log.d("loadSuggestedUsers", "2")
        users.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
//                    Log.d("loadSuggestedUsers", "3: ENTER when success")
                    // all but current user
                    val u = result.data!!.filter { it.id != userId }
//                    Log.d("loadSuggestedUsers", "4")
                    val friends = result.data.findLast { it.id == userId }!!.friends
//                    Log.d("loadSuggestedUsers", "5: ${friends.size}")
                    _state.value = _state.value.copy(
                        users = u.filter { !friends.contains(it.id) }, // result.data ?: emptyList<User>(),
//                        matchingUsers = u, // result.data ?: emptyList<User>(),
                        isLoading = false,
                        error = ""
                    )
                    Log.d("loadSuggestedUsers", "FINAL: ${friends.size}")
                }
                is DataWrapper.Loading -> {
                    Log.i(DebugConstants.RESOURCE_LOADING, "Loading loadSuggestedUsers")
                    _state.value = state.value.copy(
                        isLoading = true,
                        error = ""
                    )
                }
                is DataWrapper.Error -> {
                    Log.d(DebugConstants.PEEK, "Error loadSuggestedUsers")
                    _state.value = AddFriendState(
                        currentUserId = userId,
                        error = result.message ?: "Unknown error"
                    )
                }

            }
        } .launchIn(viewModelScope)
    }

    private fun loadFriendRequest() {
        val currentUserId: String = _state.value.currentUserId
        val request = getUserFriendRequestsUseCase(currentUserId)

        request.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    Log.d(DebugConstants.PEEK, "AddFriendViewModel.loadFriendRequest() - success - start")

                    val users: Set<User> = _state.value.users.toSet()
//                        Log.d(DebugConstants.PEEK, "Success loadFriendRequest1 + request users size ${users.size}")
                    val requests = result.data!!
//                        Log.d(DebugConstants.PEEK, "Success loadFriendRequest2 + requests size: ${requests.size}")
//                    val recipients = requests.map { it.recipientId }

//                    Log.i(DebugConstants.IN_PROGRESS, "loadFriendRequest - current user ${currentUserId}")
//
//                    Log.d(DebugConstants.IN_PROGRESS, "loadFriendRequest - List of users (${users.size})")
//                    users.forEach { it -> Log.i(DebugConstants.IN_PROGRESS, "userID: ${it.id.takeLast(8)},\t Username: ${it.username}" ) }
//
//                    Log.d(DebugConstants.IN_PROGRESS, "loadFriendRequest - List of requests (${requests.size})")
//                    requests.forEach {  Log.i(DebugConstants.IN_PROGRESS, "Sender: ${it.senderId.takeLast(8)},\t Recipient:${it.recipientId.takeLast(8)}") }

                    val incomingRequests: Set<FriendRequest> = requests
                        .filter { it.recipientId == currentUserId }
                        .toSet()
//                    Log.d(DebugConstants.PEEK, "Success loadFriendRequest6")

                    val outgoingRequests: Set<FriendRequest> = requests
                        .filter { it.senderId == currentUserId }
                        .toSet()

//                    Log.d(DebugConstants.PEEK, "Success loadFriendRequest7")

                    _state.value = _state.value.copy(
                        requests = requests, // todo check if is it possible to be null
//                        addedUsers = userIds.filter { recipients.contains(it) }, // TODO set empty set?
                        outgoingRequests = outgoingRequests,
                        incomingRequests = incomingRequests,
                        isLoading = false,
                        error = "",
                    )

                    Log.d(DebugConstants.PEEK, "AddFriendViewModel.loadFriendRequest() - success - END")
                }
                is DataWrapper.Loading -> {
                    Log.i(DebugConstants.RESOURCE_LOADING, "Loading loadFriendRequest")
                    _state.value = state.value.copy(
                        isLoading = true,
                        error = ""
                    )
                }
                is DataWrapper.Error -> {
                    Log.e(DebugConstants.PEEK, "Error loadFriendRequest()")
                    _state.value = state.value.copy(
                        isLoading = false,
                        error = result.message ?: "Unknown error"
                    )
                }

            }
        } .launchIn(viewModelScope)
    }

    private fun loadFriends() {
        val userId: String = _state.value.currentUserId
        val friends = getUserFriendsUseCase(userId)

        friends.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    Log.d(DebugConstants.PEEK, "Success loadFriends")
                    val friendsIds: List<Friend> = result.data!!
                    _state.value = _state.value.copy(
                        friends = friendsIds.map { it.id },
                        isLoading = false,
                        error = ""
                    )
                }
                is DataWrapper.Loading -> {
                    Log.i(DebugConstants.RESOURCE_LOADING, "Loading loadFriends")
                    _state.value = state.value.copy(
                        isLoading = true,
                        error = ""
                    )
                }
                is DataWrapper.Error -> {
                    Log.d(DebugConstants.PEEK, "Error loadFriends")
                    _state.value = state.value.copy(
                        isLoading = false,
                        error = result.message ?: "Unknown error"
                    )
                }

            }
        } .launchIn(viewModelScope)
    }

    private fun groupUsers() {

        val stateValue = _state.value
//        val requests = stateValue.requests

//        Log.d(DebugConstants.PEEK, "groupUsers - request size: ${requests.size}")
//        requests.forEach { Log.d(DebugConstants.PEEK, "groupUsers - request request: ${it.requestId}") }

        val incomingRequests = stateValue.incomingRequests
        val outgoingRequests = stateValue.outgoingRequests
//        val friends = stateValue.friends

//        val strangers = users
//            .filterNot { incomingRequests.keys.contains(it.id) }
//            .filterNot { outgoingRequests.keys.contains(it.id) }
//            .filterNot { friends.contains(it.id) }

        _state.value = stateValue.copy(
            incomingRequests = incomingRequests,
            outgoingRequests = outgoingRequests,

            isLoading = false,
            error = "",
        )

    }

}