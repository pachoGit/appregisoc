package com.pacho.appregisoc.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

@Composable
actual fun rememberImagePickerLauncher(
    onResult: (ImagePickerResult) -> Unit
): () -> Unit {
    val scope = rememberCoroutineScope()
    return {
        scope.launch {
            val result = withContext(Dispatchers.IO) {
                val chooser = JFileChooser()
                chooser.dialogTitle = "Seleccionar imagen"
                chooser.fileFilter = FileNameExtensionFilter(
                    "Imágenes (*.jpg, *.jpeg, *.png)",
                    "jpg", "jpeg", "png"
                )
                val returnVal = chooser.showOpenDialog(null)
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    val file = chooser.selectedFile
                    try {
                        ImagePickerResult(
                            bytes = file.readBytes(),
                            fileName = file.name
                        )
                    } catch (e: Exception) {
                        ImagePickerResult(error = "Error al leer el archivo: ${e.message}")
                    }
                } else {
                    ImagePickerResult(error = "Selección cancelada")
                }
            }
            onResult(result)
        }
    }
}
