package com.example.server.service

import com.example.server.exceptions.*
import com.example.server.model.FriendRequest
import com.example.server.repository.FriendRequestRepository
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import kotlin.jvm.optionals.getOrNull

@Service
class FriendRequestService(
    private val friendRequestRepository: FriendRequestRepository,
) {

//    @Autowired @Lazy
//    private val _userService: UserService? = null
//    private val userService: UserService by lazy { _userService!! }

    @Autowired @Lazy
    private lateinit var _userService: UserService
    private val userService: UserService by lazy { _userService }



    fun createWithoutSave(senderId: String, recipientId: String): FriendRequest {
        // TODO blocked implementation
//        if(userService.isUserBlockedByGivenUser(recipientId, senderId))
//            throw UserBlockedByGivenUserException()
        return FriendRequest(
            senderId = ObjectId(senderId),
            recipientId = ObjectId(recipientId),
            created = Instant.now()
        )
    }

    fun findAllRequestsForUserAsSender(userId: String): Set<FriendRequest> {
        return friendRequestRepository.findAllBySenderId(ObjectId(userId))
    }

    fun findAllRequestsForUserAsRecipient(userId: String): Set<FriendRequest> {
        return friendRequestRepository.findAllByRecipientId(ObjectId(userId))
    }

    fun doesRequestExistForUsers(userId1: String, userId2: String): Boolean {
        return friendRequestRepository.existsByUserIds(ObjectId(userId1), ObjectId(userId2))
    }

    fun doesRequestExistForUsers(userId1: ObjectId, userId2: ObjectId): Boolean {
        return friendRequestRepository.existsByUserIds(userId1, userId2)
    }


    fun save(friendRequest: FriendRequest): FriendRequest {
        if(doesRequestExistForUsers(friendRequest.senderId, friendRequest.recipientId))
            throw FriendRequestAlreadyExistsException()
        return friendRequestRepository.save(friendRequest)
    }

    fun findById(friendRequestId: String): FriendRequest {
        return friendRequestRepository.findById(friendRequestId).getOrNull()
            ?: throw FriendRequestNotFoundException(ErrorMessageCommons.notFound(ClassName.FRIEND_REQUEST, Field.ID, friendRequestId))
    }

    @Transactional
    fun delete(request: FriendRequest) {
        friendRequestRepository.delete(request)
    }

    @Transactional
    fun deleteRequest(friendRequestId: String) {
        val request = findById(friendRequestId)
        delete(request)
    }

    fun findAllRequestsForUser(userId: String): List<FriendRequest> {
//        return friendRequestRepository.findAllByRecipientId(ObjectId(userId))
        return findAllRequestsForUserAsRecipient(userId).toList() + findAllRequestsForUserAsSender(userId).toList()
    }

}
