package com.example.chatexu

import android.content.Context
import com.example.chatexu.data.LocalDB
import com.example.chatexu.data.daos.ChatRowDao
import com.example.chatexu.data.models.ChatRow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.random.Random

class Repository(context: Context) {

    private val _context = context

    // TODO should inner classes be singletons?
    inner class ChatRowRepo : ChatRowDao {

        private val chatRowDao = LocalDB.getLocalDB(_context).chatRowDao()

        override suspend fun insertAll(chatRows: List<ChatRow>) = withContext(Dispatchers.IO) {
            chatRowDao.insertAll(chatRows)
        }

        override suspend fun insert(chatRow: ChatRow) {
            chatRowDao.insert(chatRow)
        }

        override suspend fun delete(chatRows: List<ChatRow>) = withContext(Dispatchers.IO) {
            chatRowDao.delete(chatRows)
        }

        override fun getAll(): Flow<List<ChatRow>> {
            return chatRowDao.getAll()
        }

        override suspend fun drop() = withContext(Dispatchers.IO) {
            chatRowDao.drop()
        }

    }

    fun fetchChatList(): List<String> {
//        val inputStream = applicationContext.resources.openRawResource(R.raw.testtenmoj)
//
//        // Read img
//        val bitmap: android.graphics.Bitmap = BitmapFactory.decodeStream(inputStream)
//
//        // Close what is open
//        inputStream.close()
//
//        bitmap.scale(64,64)
//
//        return Stream.generate {
//            ChatViewData(
//                bitmap,
//                "name${Random.nextInt(0, 100)}",
//                "message${Random.nextInt(0, 100)}"
//            )   }
//            .limit(n.toLong())
//            .toList()
//
//

        return generateSequence {
            "name${Random.nextInt(0, 100)}"
        }
            .take(50)
            .toList()

    }


}