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
import com.example.chatexu.converters.UserMapper
import com.example.chatexu.domain.model.FriendRequest
import com.example.chatexu.domain.use_case.debug.get_all_users.GetAllUsersUseCase
import com.example.chatexu.domain.use_case.delete_friend_request_use_case.DeleteFriendRequestUseCase
import com.example.chatexu.domain.use_case.get_user_friend_requests_use_case.GetUserFriendRequestsUseCase
import com.example.chatexu.domain.use_case.get_user_friends.GetUserFriendsUseCase
import com.example.chatexu.domain.use_case.get_users_by_part_of_nickname_use_case.GetUsersByPartOfNicknameUseCase
import com.example.chatexu.domain.use_case.post_friend_request_use_case.PostFriendRequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class AddFriendViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val deleteFriendRequestUseCase: DeleteFriendRequestUseCase,
    private val postFriendRequestUseCase: PostFriendRequestUseCase,
    private val getUserFriendRequestsUseCase: GetUserFriendRequestsUseCase,
    private val getUserFriendsUseCase: GetUserFriendsUseCase,
    private val getUsersByPartOfNicknameUseCase: GetUsersByPartOfNicknameUseCase,

    ): ViewModel() {

    private val _state = mutableStateOf<AddFriendState>(AddFriendState())
    val state: State<AddFriendState> = _state

    init {
        _state.value = _state.value.copy(
            userId = savedStateHandle.get<String>(Constants.PARAM_USER_ID)
                ?: Constants.ID_DEFAULT //TODO what if null
        )

        loadSuggestedUsers()
        loadFriendRequest()
        loadFriends()

        groupUsers()
    }

    fun filterUsersByNickname(partOfNickname: String) {

        val trimmedPartOfNickname = partOfNickname.trim()

//        _state.value = _state.value.copy(
//            // TODO change to not fetching all users from database and filtering after 1 sec after last input
//            matchingUsers = state.value.users.filter { it.nickname.contains(trimmedPartOfNickname, ignoreCase = true) },
//            isLoading = false,
//            error = ""
//        )

        fetchAndGroupUsersByPhrase(trimmedPartOfNickname)

    }

    private fun fetchAndGroupUsersByPhrase(partOfNickname: String) {

        val users = getUsersByPartOfNicknameUseCase(_state.value.userId, partOfNickname)

        users.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    Log.d(DebugConstants.PEEK, "Success fetchUsersByPhrase, ${result.data!!.size}")
                    _state.value = _state.value.copy(
                        users = result.data,
                        isLoading = false,
                        error = ""
                    )
                    groupUsers()
                }
                is DataWrapper.Loading -> {
                    Log.d(DebugConstants.PEEK, "Loading fetchUsersByPhrase")
                    _state.value = state.value.copy(
                        isLoading = true,
                        error = ""
                    )
                }
                is DataWrapper.Error -> {
                    Log.d(DebugConstants.PEEK, "Error fetchUsersByPhrase")
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message ?: "Unknown error"
                    )
                }
            }
        } .launchIn(viewModelScope)
    }

    fun sendFriendRequest(userToAddId: String)  {
        Log.d(DebugConstants.PEEK, "Add user $userToAddId")

        val request = postFriendRequestUseCase(state.value.userId, userToAddId)

        request.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    Log.d(DebugConstants.PEEK, "Success sendFriendRequest, ${result.data}")
                    _state.value = _state.value.copy(
                        requests = _state.value.requests + result.data!!,
                        addedUsers = _state.value.addedUsers + userToAddId
                    )
                }
                is DataWrapper.Loading -> {
                    Log.d(DebugConstants.PEEK, "Loading sendFriendRequest")
                    _state.value = state.value.copy(
                        isLoading = true,
                        error = ""
                    )
                }
                is DataWrapper.Error -> {
                    Log.d(DebugConstants.PEEK, "Error sendFriendRequest")
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message ?: "Unknown error"
                    )
                }
            }
        } .launchIn(viewModelScope)

    }

    fun deleteFriendRequest(request: FriendRequest) {

        val requestId = request.requestId
        Log.d(DebugConstants.PEEK, "Delete request $requestId")

        val success = deleteFriendRequestUseCase(requestId)

        success.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    Log.d(DebugConstants.PEEK, "Success sendFriendRequest, ${result.data}")
                    _state.value = _state.value.copy(
                        requests = _state.value.requests.filter { it.requestId != requestId },
                        addedUsers = _state.value.addedUsers.filter { it != request.recipientId }
                    )
                }
                is DataWrapper.Loading -> {
                    Log.d(DebugConstants.PEEK, "Loading sendFriendRequest")
                    _state.value = state.value.copy(
                        isLoading = true,
                        error = ""
                    )
                }
                is DataWrapper.Error -> {
                    Log.d(DebugConstants.PEEK, "Error deleteFriendRequest")
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message ?: "Unknown error"
                    )
                }
            }
        } .launchIn(viewModelScope)

    }

    private fun loadSuggestedUsers() {
        val userId: String = _state.value.userId
        val users = getAllUsersUseCase()
        users.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    Log.d(DebugConstants.PEEK, "Success loadSuggestedUsers")
                    // all but current user
                    val u = result.data!!.filter { it.id != userId }
                    val friends = result.data.findLast { it.id == userId }!!.friends

                    _state.value = _state.value.copy(
                        users = u.filter { !friends.contains(it.id) }, // result.data ?: emptyList<User>(),
                        matchingUsers = u, // result.data ?: emptyList<User>(),
                        isLoading = false,
                        error = ""
                    )
                }
                is DataWrapper.Loading -> {
                    Log.d(DebugConstants.PEEK, "Loading loadSuggestedUsers")
                    _state.value = state.value.copy(
                        isLoading = true,
                        error = ""
                    )
                }
                is DataWrapper.Error -> {
                    Log.d(DebugConstants.PEEK, "Error loadSuggestedUsers")
                    _state.value = AddFriendState(
                        userId = userId,
                        error = result.message ?: "Unknown error"
                    )
                }

            }
        } .launchIn(viewModelScope)
    }

    private fun loadFriendRequest() {
        val userId: String = _state.value.userId
        val request = getUserFriendRequestsUseCase(userId)
        request.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    Log.d(DebugConstants.PEEK, "Success loadFriendRequest")

                    val userIds = _state.value.users.map { it.id }
                    val requests = result.data!!
                    val recipients = requests.map { it.recipientId }

                    val users = _state.value.users

                    val incomingRequests = requests
                        .filter { it.recipientId == userId }
                        .associateBy { request -> users.find { user -> user.id == request.senderId }!! }
                    val outgoingRequests = requests
                        .filter { it.senderId == userId }
                        .associateBy { request -> users.find { user -> user.id == request.recipientId }!! }

                    _state.value = _state.value.copy(
                        requests = requests, // todo check if possible to be null
                        addedUsers = userIds.filter { recipients.contains(it) },
                        outgoingRequests = outgoingRequests,
                        incomingRequests = incomingRequests,
                        isLoading = false,
                        error = "",
                    )

                }
                is DataWrapper.Loading -> {
                    Log.d(DebugConstants.PEEK, "Loading loadFriendRequest")
                    _state.value = state.value.copy(
                        isLoading = true,
                        error = ""
                    )
                }
                is DataWrapper.Error -> {
                    Log.d(DebugConstants.PEEK, "Error loadFriendRequest")
                    _state.value = state.value.copy(
                        isLoading = false,
                        error = result.message ?: "Unknown error"
                    )
                }

            }
        } .launchIn(viewModelScope)
    }

    private fun loadFriends() {
        val userId: String = _state.value.userId
        val friends = getUserFriendsUseCase(userId)
        friends.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    Log.d(DebugConstants.PEEK, "Success loadFriends")
                    _state.value = _state.value.copy(
                        friends = result.data!!.map { UserMapper.toUser(it) }
                    )
                }
                is DataWrapper.Loading -> {
                    Log.d(DebugConstants.PEEK, "Loading loadFriends")
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

        val state = _state.value
        val userId = state.userId

        val users = state.users
        val requests = state.requests

        Log.d(DebugConstants.PEEK, "groupUsers - request size: ${requests.size}")
        requests.forEach {
            Log.d(DebugConstants.PEEK, "groupUsers - request request: ${it.requestId}")
        }

        val incomingRequests = state.incomingRequests
        val outgoingRequests = state.outgoingRequests
        val friends = state.friends
        val strangers = users
            .filterNot {
                incomingRequests.keys
                    .map { user -> user.id }
                    .contains(it.id) }
            .filterNot {
                outgoingRequests.keys
                .map { user -> user.id }
                .contains(it.id) }
            .filterNot {
                friends
                    .map { friend -> friend.id }
                    .contains(it.id)
            }

        _state.value = state.copy(
            incomingRequests = incomingRequests,
            outgoingRequests = outgoingRequests,
            strangers = strangers,

            isLoading = false,
            error = "",
        )

    }

}