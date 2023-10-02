package com.example.chatexu

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.graphics.scale
import com.example.chatexu.data.models.ChatRow
import com.example.chatexu.data.remote.RemoteSource
import retrofit2.Response
import java.time.Instant
import java.util.UUID
import kotlin.math.abs
import kotlin.random.Random

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
//    inner class ChatRowRepo {
    companion object ChatRowRepo {


//        private val chatRowDao = LocalDB.getLocalDB(_context).chatRowDao()
        private val api = RemoteSource.apiChatRow
        private val testRows = mutableListOf<ChatRow>()
//        private val testRows = mutableListOf<ChatRow>()

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

        suspend fun testChatList(context: Context, n: Int = 50): List<ChatRow> {

            Log.d("HERE", "test1 + ${this.hashCode()}")
            if(testRows.isNotEmpty()) {
                Log.d("HERE", "test2")
                return testRows
            }
            Log.d("HERE", "test3")

            val l = mutableListOf<ChatRow>()
            val chatNames = listOf(
                "Kryska", "setfek kozak fest", "pomoniaki",
                "Dluga nazwa chatu tak aby sie zepsulo a bo jak",
                "Alexander Kowalczyk",  "Paweł Jaworski",  "Heronim Borkowski",
                "Gabriel Wojciechowski",  "Jędrzej Przybylski",  "Emilia Kamińska",
                "Józefa Sawicka",  "Dagmara Sokołowska",  "Ada Jaworska",
                )
            val wordCounts: () -> Int = { abs(Random.nextInt()) % 10 + 1 }
            val worldLength = { abs(Random.nextInt()) % 13 + 3 }
            val icon = {
                val raw = listOf(R.raw.blue, R.raw.red, R.raw.green).random()
                val inputStream = context.resources.openRawResource(raw)
                val bitmap: android.graphics.Bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
//                bitmap.scale(32,32)
                bitmap
            }



            for (i in 0..n)
                l.add(
                    ChatRow.Builder()
                        .chatId(UUID.randomUUID())
                        .chatName(chatNames.random())
                        .timestamp(Instant.now())
                        .lastMessage(
                            generateSequence { "s".repeat(worldLength()) }
                                .take(wordCounts())
                                .fold("") {acc, i -> "$acc$i " }
                        )
                        .icon(icon())
                        .build()
                )

            testRows.addAll(l)
            return testRows
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