package com.pacho.appregisoc.ui.components

import androidx.compose.runtime.Composable

@Composable
actual fun rememberImagePickerLauncher(
    onResult: (ImagePickerResult) -> Unit
): () -> Unit {
    return {
        onResult(
            ImagePickerResult(error = "Selección de fotos no disponible en Web (simulado)")
        )
    }
}
