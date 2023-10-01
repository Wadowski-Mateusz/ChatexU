package com.example.chatexu.data.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import java.util.UUID

class UUIDAdapter: JsonAdapter<UUID>(){
    @FromJson
    override fun fromJson(reader: JsonReader): UUID? {
        return UUID.fromString(reader.readJsonValue().toString())
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: UUID?) {
        writer.jsonValue(value)
    }
}