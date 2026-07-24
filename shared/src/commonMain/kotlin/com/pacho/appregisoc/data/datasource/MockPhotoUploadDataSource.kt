package com.pacho.appregisoc.data.datasource

import com.pacho.appregisoc.core.Result
import kotlinx.coroutines.delay

class MockPhotoUploadDataSource : PhotoUploadDataSource {

    private var uploadCounter = 0

    override suspend fun uploadPhoto(imageBytes: ByteArray, fileName: String): Result<String> {
        return try {
            delay(1500L)
            uploadCounter++
            val fakeUrl = "https://storage.appregisoc.com/photos/${uploadCounter}_${fileName}"
            Result.Success(fakeUrl)
        } catch (e: Exception) {
            Result.Error("Error al subir foto: ${e.message}")
        }
    }
}
