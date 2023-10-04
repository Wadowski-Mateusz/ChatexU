package com.example.chatexu

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import com.example.chatexu.domain.model.ChatRow
import com.example.chatexu.data.remote.RemoteSource
import retrofit2.Response
import java.time.Instant
import java.util.UUID
import kotlin.math.abs
import kotlin.random.Random

class Repository(context: Context) {

    private val testRows = mutableListOf<ChatRow>()

    suspend fun testChatList(context: Context, n: Int = 50): List<ChatRow> {
        if(testRows.isNotEmpty())
                return testRows

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
//          bitmap.scale(32,32)
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