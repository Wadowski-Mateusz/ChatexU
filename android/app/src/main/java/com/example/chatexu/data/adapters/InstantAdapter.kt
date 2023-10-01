package com.example.chatexu.data.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import java.time.Instant

class InstantAdapter: JsonAdapter<Instant>(){
    @FromJson
    override fun fromJson(reader: JsonReader): Instant? {
        return Instant.parse(reader.readJsonValue().toString())
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: Instant?) {
        writer.jsonValue(value)
    }
}