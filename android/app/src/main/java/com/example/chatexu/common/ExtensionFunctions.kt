package com.example.chatexu.common

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.mongodb.kbson.ObjectId
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

// Returns the same ObjectId every time
fun ObjectId.default(): ObjectId = ObjectId("000000000000000000000000")

fun ObjectId.isStringValid(value: String): Boolean {
    return try {
        ObjectId(value)
        true
    } catch (e: IllegalArgumentException) {
        false
    } catch (e: Exception) {
        throw e
    }
}

fun Uri.toMultipartBodyPart(uri: Uri, context: Context, partName: String): MultipartBody.Part {
    val paramName: String = partName // backend waits for multipart file with this name
    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(uri) ?: throw NotFoundException("Given icon not found")
    val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir) // Create a temporary file
    val outputStream = FileOutputStream(tempFile)

    // Copy the content from the URI to the temporary file
    inputStream.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }

    val requestBody = tempFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(paramName, tempFile.name, requestBody)
}