package com.example.modelobase.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

/** Guarda fotos opcionais em diretório interno privado. Nunca exibe fotos no mapa público. */
object PhotoStore {
    fun importPhoto(context: Context, uri: Uri): String {
        val mime = context.contentResolver.getType(uri) ?: "image/jpeg"
        require(mime in setOf("image/jpeg", "image/png", "image/webp")) { "Formato de imagem não suportado." }
        val extension = when (mime) { "image/png" -> "png"; "image/webp" -> "webp"; else -> "jpg" }
        val parent = File(context.filesDir, "evidencias").apply { mkdirs() }
        val output = File(parent, "img_${UUID.randomUUID()}.$extension")
        try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(output).use { stream ->
                    val buffer = ByteArray(8_192)
                    var count = 0L
                    while (true) {
                        val n = input.read(buffer)
                        if (n < 0) break
                        count += n
                        require(count <= 10_485_760L) { "A foto deve ter no máximo 10 MB." }
                        stream.write(buffer, 0, n)
                    }
                    require(count > 0) { "O arquivo de imagem está vazio." }
                }
            } ?: error("Não foi possível abrir a imagem.")
            return output.absolutePath
        } catch (ex: Exception) {
            output.delete()
            throw ex
        }
    }

    fun thumbnail(path: String?): Bitmap? {
        if (path == null || !File(path).isFile) return null
        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(path, options)
        options.inSampleSize = maxOf(1, maxOf(options.outWidth, options.outHeight) / 700)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(path, options)
    }
}
