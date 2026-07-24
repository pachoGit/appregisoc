package com.pacho.appregisoc.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
actual fun rememberImagePickerLauncher(
    onResult: (ImagePickerResult) -> Unit
): () -> Unit {
    val scope = rememberCoroutineScope()
    return {
        scope.launch {
            onResult(
                ImagePickerResult(error = "Selección de fotos no disponible en iOS (simulado)")
            )
        }
    }
}
