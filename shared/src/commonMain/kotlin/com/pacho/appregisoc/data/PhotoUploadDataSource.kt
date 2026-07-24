package com.pacho.appregisoc.data

import com.pacho.appregisoc.core.Result

interface PhotoUploadDataSource {
    suspend fun uploadPhoto(imageBytes: ByteArray, fileName: String): Result<String>
}
