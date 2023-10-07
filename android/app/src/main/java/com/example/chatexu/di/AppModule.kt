package com.example.chatexu.di

import com.example.chatexu.common.Constants
import com.example.chatexu.data.adapters.InstantAdapter
import com.example.chatexu.data.adapters.UUIDAdapter
import com.example.chatexu.data.remote.ChatApi
import com.example.chatexu.data.repository.ChatRepositoryImpl
import com.example.chatexu.domain.model.ChatRow
import com.example.chatexu.domain.repository.ChatRepository
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideChatApi(): ChatApi {
//        return Retrofit.Builder()
//            .baseUrl(Constants.BASE_URL)
//            .addConverterFactory(
//                MoshiConverterFactory.create(
//                Moshi.Builder()
//                    .add(UUIDAdapter())
//                    .add(InstantAdapter())
//                    .add(GsonConverterFactory.create())
//                    .build()
//            ))
//            .build()
//            .create(ChatApi::class.java)
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChatApi::class.java)
    }

    @Provides
    @Singleton
    fun provideChatRepository(api: ChatApi): ChatRepository {
        return ChatRepositoryImpl(api)
    }

}
