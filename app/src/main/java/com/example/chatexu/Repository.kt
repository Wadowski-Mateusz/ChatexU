package com.example.chatexu

import kotlin.random.Random

class Repository {

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