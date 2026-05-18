package com.example.kutirakone.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

object ImageConverter {

    // ✅ Convert Bitmap → Base64 (OPTIMIZED FOR FIRESTORE)
    fun bitmapToBase64(bitmap: Bitmap): String {

        // 🔥 Resize more (reduce size)
        val resized = Bitmap.createScaledBitmap(bitmap, 250, 250, true)

        val outputStream = ByteArrayOutputStream()

        // 🔥 Reduce quality (VERY IMPORTANT)
        resized.compress(Bitmap.CompressFormat.JPEG, 20, outputStream)

        val byteArray = outputStream.toByteArray()

        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    // ✅ Convert Base64 → Bitmap safely
    fun base64ToBitmap(base64: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            null
        }
    }
}