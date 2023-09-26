package com.example.chatexu.data

import android.content.Context
import androidx.room.BuiltInTypeConverters
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.chatexu.data.daos.ChatRowDao
import com.example.chatexu.data.models.ChatRow


@Database(entities = [ChatRow::class], version = 1)
@TypeConverters(
    Converters::class,
//    builtInTypeConverters = BuiltInTypeConverters(
//        uuid = BuiltInTypeConverters.State.ENABLED,
//        enums = BuiltInTypeConverters.State.ENABLED
//    )
)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun chatRowDao(): ChatRowDao
}

object LocalDB {
    private var db: LocalDatabase? = null

    fun getLocalDB(context: Context): LocalDatabase {
        if (db == null)
            db = Room
                .databaseBuilder(context, LocalDatabase::class.java, "Local Database")
                .build()
        return db!!
    }
}
