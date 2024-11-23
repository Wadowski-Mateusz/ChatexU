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


        loadSuggestedUsers()
        Thread.sleep(75) // dirty fix for async problem - when invoking loadFriendRequest() for the second time, for some reason it is not waiting for loadSuggestedUsers()
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

        val users = getUsersByPartOfNicknameUseCase(_state.value.currentUserId, partOfNickname)

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
        Log.d(DebugConstants.PEEK, "AddFriendViewModel.sendFriendRequest() - send request to user $userToAddId")

        val request = postFriendRequestUseCase(state.value.currentUserId, userToAddId)

        request.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    Log.d(DebugConstants.PEEK, "AddFriendViewModel.sendFriendRequest() - enter success, ${result.data}")

                    val friendRequestFromResponse: FriendRequest = result.data!!

                    _state.value = _state.value.copy(
                        requests = _state.value.requests.plus(friendRequestFromResponse),
                        outgoingRequests = _state.value.outgoingRequests.plus(_state.value.currentUserId to friendRequestFromResponse),
                        isLoading = false,
                        error = ""
                    )

                    // TODO
                    // bardzo brzydkie odświeżenie listy użytkowników
                    // na liście użytkowników do dodania klikamy kogoś
                    // nic się nie odświeża, ale request do bazy danych poszedł i się wykonał
                    // poniższe sprawia, że lista zostaje odświeżona
                    loadFriendRequest()
                }
                is DataWrapper.Loading -> {
                    Log.d(DebugConstants.PEEK, "AddFriendViewModel.sendFriendRequest() - Loading")
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
                        outgoingRequests = _state.value.outgoingRequests.filter { it.value.requestId != requestId },
                        isLoading = false,
                        error = ""
                    )
                }
                is DataWrapper.Loading -> {
                    Log.d(DebugConstants.PEEK, "AddFriendViewModel.deleteFriendRequest() - Loading")
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
        Log.d(DebugConstants.IN_PROGRESS, "AddFriendViewModel.acceptFriendRequest()")

        val response = postAcceptFriendRequestUseCase(friendRequest.requestId)

        response.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    Log.d(DebugConstants.PEEK, "AddFriendViewModel.acceptFriendRequest() - Enter success acceptFriendRequest, ${result.data}")
                    _state.value = _state.value.copy(
                        incomingRequests = _state.value.incomingRequests.filter { it.value != friendRequest },
                        acceptedRequests = _state.value.acceptedRequests.plus(friendRequest),
                        friends = _state.value.friends.plus(friendRequest.senderId),
                        isLoading = false,
                        error = ""
                    )
                }
                is DataWrapper.Loading -> {
                    Log.d(DebugConstants.PEEK, "AddFriendViewModel.acceptFriendRequest() - Loading")
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
        Log.w(DebugConstants.IN_PROGRESS, "AddFriendViewModel.rejectFriendRequest()")

        val response = deleteRejectFriendRequestUseCase(friendRequest.requestId)

        response.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    Log.d(DebugConstants.PEEK, "AddFriendViewModel.rejectFriendRequest() - Enter success acceptFriendRequest, ${result.data}")
                    _state.value = _state.value.copy(
                        requests = _state.value.requests.filter { it != friendRequest },
                        incomingRequests = _state.value.incomingRequests.filter { it.value != friendRequest },
                        isLoading = false,
                        error = ""
                    )
                }
                is DataWrapper.Loading -> {
                    Log.d(DebugConstants.PEEK, "AddFriendViewModel.rejectFriendRequest() - Loading")
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
                        matchingUsers = u, // result.data ?: emptyList<User>(),
                        isLoading = false,
                        error = ""
                    )
                    Log.d("loadSuggestedUsers", "FINAL: ${friends.size}")
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
                    Log.d(DebugConstants.PEEK, "ENTER loadFriendRequest")

                    val users = _state.value.users
//                        Log.d(DebugConstants.PEEK, "Success loadFriendRequest1 + request users size ${users.size}")
//                        users.onEach { Log.w("I did baddy sender - user", it.id) }
                    val userIds = users.map { it.id }
//                        Log.d(DebugConstants.PEEK, "Success loadFriendRequest2")
                    val requests = result.data!!
//                        Log.d(DebugConstants.PEEK, "Success loadFriendRequest3 + requests size: ${requests.size} + ")
                    val recipients = requests.map { it.recipientId }
//                        Log.d(DebugConstants.PEEK, "Success loadFriendRequest4 + recipients.size = ${recipients.size}")

//                    // TODO TU SIE WYWALA
//                    val incomingRequests: Map<String, FriendRequest> = requests
//                        .filter { it.recipientId == userId }
//                        .associateBy { request ->
////                            Log.d("I did baddy sender", "senderID ${request.senderId}")
//                            users.find { user -> user.id == request.senderId }!!
//                        }
//                    Log.d(DebugConstants.PEEK, "Success loadFriendRequest5")

                    val incomingRequests: Map<String, FriendRequest> = requests
                        .filter { it.recipientId == currentUserId }
                        .associateBy { request ->
                            users.map { it.id }.find { userID -> userID == request.senderId }!!
                        }

                    val outgoingRequests: Map<String, FriendRequest> =  requests
                        .filter { it.senderId == currentUserId }
                        .associateBy { request ->
                            users.map { it.id }.find { userID -> userID == request.recipientId }!!
                        }
//                        requests
//                        .filter { it.senderId == userId }
//                        .associateBy { request ->
//                            users.find { user ->
////                                Log.d("I did baddy recipient", "${user.id} == ${request.recipientId}")
//                                user.id == request.recipientId
//                            }!!
//                        }

//                    Log.d(DebugConstants.PEEK, "Success loadFriendRequest7 ${outgoingRequests.size}")

                    _state.value = _state.value.copy(
                        requests = requests, // todo check if possible to be null
//                        addedUsers = userIds.filter { recipients.contains(it) },
                        outgoingRequests = outgoingRequests,
                        incomingRequests = incomingRequests,
                        isLoading = false,
                        error = "",
                    )
                    Log.d(DebugConstants.PEEK, "Success loadFriendRequest FINAL")

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
        val userId = state.currentUserId

        val users = state.users
        val requests = state.requests

        Log.d(DebugConstants.PEEK, "groupUsers - request size: ${requests.size}")
        requests.forEach {
            Log.d(DebugConstants.PEEK, "groupUsers - request request: ${it.requestId}")
        }

        val incomingRequests = state.incomingRequests
        val outgoingRequests = state.outgoingRequests
        val friends = state.friends
//        val strangers = users
//            .filterNot {
//                incomingRequests.keys
//                    .map { user -> user.id }
//                    .contains(it.id) }
//            .filterNot {
//                outgoingRequests.keys
//                .map { user -> user.id }
//                .contains(it.id) }
//            .filterNot {
//                friends
//                    .map { friend -> friend.id }
//                    .contains(it.id)
//            }

        val strangers = users
            .filterNot { incomingRequests.keys.contains(it.id) }
            .filterNot { outgoingRequests.keys.contains(it.id) }
            .filterNot { friends.contains(it.id) }


        _state.value = state.copy(
            incomingRequests = incomingRequests,
            outgoingRequests = outgoingRequests,

            isLoading = false,
            error = "",
        )

    }

}