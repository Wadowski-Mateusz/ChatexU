package com.example.chatexu.di

import android.util.Log
import com.example.chatexu.common.Constants
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.data.remote.ChatApi
import com.example.chatexu.data.repository.ChatRepositoryImpl
import com.example.chatexu.domain.repository.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private object UsedApi {
        const val TEST_API = "TEST"
        const val PRODUCTION_API = "PRODUCTION"

//        const val API = TEST_API
        const val API = PRODUCTION_API
    }

    @Provides
    @Singleton
    fun provideChatApi(): ChatApi {

        Log.w(
            /* tag = */ DebugConstants.POTENTIAL_BUG,
            /* msg = */
            "If cannot connect to the server, check the base url." +
                    "Use 'ipconfig' in cmd to get new base url."
        )

        val url: String = when (UsedApi.API) {
            UsedApi.PRODUCTION_API -> {
                Log.i(DebugConstants.INFO, "Using production API.")
                Constants.URL_BACKEND
            }
//            UsedApi.TEST_API -> {
//                Log.i(DebugConstants.INFO, "Using test API.")
//                Constants.BASE_URL_TEST
//            }
            else -> throw IllegalAccessException("No url for ${UsedApi.API}.")
        }

        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(ScalarsConverterFactory.create()) // HAVE TO BE ADDED FIRST
            .addConverterFactory(JacksonConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChatApi::class.java)

    }

    @Provides
    @Singleton
    fun provideChatRepository(api: ChatApi): ChatRepository {
        return when (UsedApi.API) {
            UsedApi.PRODUCTION_API -> ChatRepositoryImpl(api)
//            UsedApi.TEST_API -> ChatRepositoryTestImpl(api)
            else -> throw IllegalAccessException("No url for ${UsedApi.API}.")
        }
    }



}
