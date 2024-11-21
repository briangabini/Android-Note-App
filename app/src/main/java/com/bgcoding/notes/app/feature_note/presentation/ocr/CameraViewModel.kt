package com.bgcoding.notes.app.feature_note.presentation.ocr

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
    val bitmaps = _bitmaps.asStateFlow()

    private val _recognizedText = MutableStateFlow("")
    val recognizedText = _recognizedText.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog = _showDialog.asStateFlow()

    fun onTakePhoto(bitmap: Bitmap) {
        _bitmaps.value += bitmap
        recognizeText(bitmap)
    }

    private fun recognizeText(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.Builder().build())

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val recognizedText = visionText.textBlocks.joinToString(separator = "\n") { it.text }
                _recognizedText.value = recognizedText
                if (recognizedText.isEmpty()) {
                    Toast.makeText(context, "No text found", Toast.LENGTH_SHORT).show()
                } else {
                    _showDialog.value = true
                }

                Log.d("TextRecognition", "Recognized text: $recognizedText")
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                Log.e("TextRecognition", "Text recognition failed", e)
            }
    }

    fun dismissDialog() {
        _showDialog.value = false
    }

    fun getContext(): Context = context
}