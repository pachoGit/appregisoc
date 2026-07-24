package com.pacho.appregisoc.ui.components

import androidx.compose.runtime.Composable

data class ImagePickerResult(
    val bytes: ByteArray? = null,
    val fileName: String? = null,
    val error: String? = null
) {
    val isSuccess: Boolean get() = bytes != null && fileName != null
}

@Composable
expect fun rememberImagePickerLauncher(
    onResult: (ImagePickerResult) -> Unit
): () -> Unit
