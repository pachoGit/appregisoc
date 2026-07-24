package com.pacho.appregisoc.domain.usecase

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.data.datasource.PhotoUploadDataSource

class UploadPhotoUseCase(
    private val photoUploadDataSource: PhotoUploadDataSource
) {
    suspend operator fun invoke(imageBytes: ByteArray, fileName: String): Result<String> {
        if (imageBytes.isEmpty()) {
            return Result.Error("No se seleccionó ninguna imagen")
        }
        return photoUploadDataSource.uploadPhoto(imageBytes, fileName)
    }
}
