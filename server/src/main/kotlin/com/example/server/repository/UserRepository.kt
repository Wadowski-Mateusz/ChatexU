package com.example.server.repository

import com.example.server.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.Update


interface UserRepository: MongoRepository<User, String> {

    fun findByEmailAndPassword(email: String, password: String): User?
    fun findByUsernameAndPassword(username: String, password: String): User?
    fun existsByNickname(fieldName: String): Boolean
    fun existsByUsername(fieldName: String): Boolean
    fun existsByEmail(fieldName: String): Boolean
    fun findByNickname(nickname: String): User?
    fun findByUsername(username: String): User?
    fun findUserByEmail(email: String): User?

    @Query("{ 'nickname': { \$regex: ?0, \$options: 'i' } }")
    fun findAllByNicknameLike(nickname: String): List<User>


//    @Query("{ 'email' : ?0 }")
//    @Update("{ '\$set' : { 'nickname' : ?1 } }")
//    fun updateNicknameByEmail(email: String?, nickname: String?): Long

    @Query("{ '_id' : ?0 }")
    @Update("{ '\$set' : { 'nickname' : ?1 } }")
    fun updateNicknameByUserId(userId: String?, nickname: String?): Long


    @Query("{ '_id' : ?0 }")
    @Update("{ '\$set' : { 'profilePictureUri' : ?1 } }")
    fun updateProfilePictureUriByUserId(userId: String?, profilePictureUri: String?): Long


    @Query("{ '_id' : ?0 }")
    @Update("{ '\$pull': { 'friends': ?1  } }")
    fun deleteFriendsByUserId(userId: String, friendId: String): Long


    @Query("{ '_id' : ?0 }")
    @Update("{ '\$set': { 'tokens': {?1 : ?2}  } }")
    fun saveNewTokensByUserId(userId: String, jwt: String, isExpired: Boolean = false): Long

    @Query("{ '_id' : ?0 }")
    @Update("{ '\$set': { 'tokens': {?1 : ?2}  } }")
    fun saveNewTokensByUserId(userId: String, jwt: List<String>, isExpired: List<Boolean>): Long



//    fun findAllByUserIdAndExpiredIsFalse(userId: UUID?): List<Token?>? // this
//    fun findByToken(token: String?): Optional<Token?>?
//        fun findByRole(role: String?): Optional<Role?>?

}