package com.example.chatexu.data.remote

import com.example.chatexu.data.adapters.InstantAdapter
import com.example.chatexu.data.adapters.UUIDAdapter
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RemoteSource {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://172.31.0.1:8091/")
//        .addConverterFactory(MoshiConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(
            Moshi.Builder()
//                .add(KotlinJsonAdapterFactory())
                .add(UUIDAdapter())
                .add(InstantAdapter())
                .build()
        ))
        .build()

    val apiChatRow = retrofit.create(ChatRowApi::class.java)
}