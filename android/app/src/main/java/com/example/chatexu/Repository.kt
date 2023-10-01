package com.example.chatexu

import android.content.Context
import android.util.Log
import com.example.chatexu.data.models.ChatRow
import com.example.chatexu.data.remote.RemoteSource
import retrofit2.Response
import java.time.Instant
import java.util.UUID

class Repository(context: Context) {

    private val _context = context
    private val api = RemoteSource.apiChatRow
//    suspend fun loadChatView(chatId: UUID, viewerId: UUID): List<ChatRow> {
//            Log.d("HERE", "repo: loadChatView")
////        return listOf(
////            ChatRow.Builder()
////                .chatId(UUID.randomUUID())
////                .chatName("asdasd")
////                .lastMessage("asdasd")
////                .timestamp(Instant.now())
////                .build()
////        )
//            return listOf(api.getChatRowFast().body()!!)
//        }

    // TODO should inner classes be singletons?
    inner class ChatRowRepo {
//    companion object ChatRowRepo {

//        private val chatRowDao = LocalDB.getLocalDB(_context).chatRowDao()
        private val api = RemoteSource.apiChatRow

        suspend fun loadChatViewResponse(chatId: UUID, viewerId: UUID): Response<ChatRow> {
            return api.getChatRow(chatId, viewerId)
        }

        suspend fun loadChatView(chatId: UUID, viewerId: UUID): List<ChatRow> {
            Log.d("HERE", "repo: loadChatView")
            return listOf(api.getChatRow(chatId, viewerId).body()!!)
        }


        suspend fun loadChatViewList(chatId: UUID, viewerId: UUID): List<ChatRow> {
            val l = mutableListOf<ChatRow>()
            for (i in 0..50)
                l.add(
                    api.getChatRow(chatId, viewerId).body()!!
                )
            return l.toList()
        }

    }

//    fun fetchChatList(): List<String> {
////        val inputStream = applicationContext.resources.openRawResource(R.raw.testtenmoj)
////
////        // Read img
////        val bitmap: android.graphics.Bitmap = BitmapFactory.decodeStream(inputStream)
////
////        // Close what is open
////        inputStream.close()
////
////        bitmap.scale(64,64)
////
////        return Stream.generate {
////            ChatViewData(
////                bitmap,
////                "name${Random.nextInt(0, 100)}",
////                "message${Random.nextInt(0, 100)}"
////            )   }
////            .limit(n.toLong())
////            .toList()
////
////
//
//        return generateSequence {
//            "name${Random.nextInt(0, 100)}"
//        }
//            .take(50)
//            .toList()
//
//    }


}