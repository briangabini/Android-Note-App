package com.bgcoding.notes.app.feature_note.presentation.ocr

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class OcrHandler() {

    fun recognizeText(bitmap: Bitmap): String {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.Builder().build())
        var recognizedText = "";

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                recognizedText = visionText.textBlocks.joinToString(separator = "\n") { it.text }
            }
            .addOnFailureListener { e ->
            }

        return recognizedText
    }
}