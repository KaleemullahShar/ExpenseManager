package com.spendwise.utils

import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import android.content.Context
import kotlinx.coroutines.tasks.await

object ReceiptScanner {
    private val amountRegex = Regex("""(?i)(total|amount|balance)?\s*[:$]?\s*([0-9]+(?:\.[0-9]{1,2})?)""")

    suspend fun extractAmount(context: Context, uri: Uri): Double? {
        val image = InputImage.fromFilePath(context, uri)
        val text = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS).process(image).await().text
        return amountRegex.findAll(text).mapNotNull { it.groupValues.getOrNull(2)?.toDoubleOrNull() }.maxOrNull()
    }
}
