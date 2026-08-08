package com.pacho.appregisoc.ui.features.coach

import com.pacho.appregisoc.ui.components.ImagePickerResult
import com.pacho.appregisoc.ui.components.PhotoPickerState

sealed class PhotoType { data object COACH : PhotoType(); data object DNI_FRONT : PhotoType(); data object DNI_BACK : PhotoType() }

fun handleImagePickerResult(
    result: ImagePickerResult,
    photoType: PhotoType?,
    onUploadPhoto: (ByteArray, String, PhotoType, (PhotoPickerState) -> Unit) -> Unit,
    onStateUpdate: (PhotoPickerState) -> Unit
) {
    if (result.isSuccess && photoType != null) {
        onUploadPhoto(result.bytes!!, result.fileName!!, photoType, onStateUpdate)
    } else if (result.error != null) {
        onStateUpdate(PhotoPickerState(error = result.error))
    }
}

fun updateFormPhotoState(formState: CoachFormState, photoType: PhotoType, newState: PhotoPickerState): CoachFormState {
    return when (photoType) {
        PhotoType.COACH -> formState.copy(photoState = newState)
        PhotoType.DNI_FRONT -> formState.copy(dniFrontPhotoState = newState)
        PhotoType.DNI_BACK -> formState.copy(dniBackPhotoState = newState)
    }
}
