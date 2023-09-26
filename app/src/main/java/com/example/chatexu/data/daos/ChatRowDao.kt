package com.example.chatexu.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.chatexu.data.models.ChatRow
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatRowDao {

    @Insert
    suspend fun insertAll(chatRows: List<ChatRow>)

    @Insert
    suspend fun insert(chatRow: ChatRow)

    @Delete
    suspend fun delete(chatRows: List<ChatRow>)

    @Query("SELECT * FROM chat_rows")
    fun getAll(): Flow<List<ChatRow>>

    @Query("DELETE FROM chat_rows")
    suspend fun drop()

}