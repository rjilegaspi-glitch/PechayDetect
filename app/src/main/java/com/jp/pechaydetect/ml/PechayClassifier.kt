package com.jp.pechaydetect.ml

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.gpu.GpuDelegate
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

/**
 * TFLite inference wrapper for the PechayDetect model.
 *
 * Supports both MobileNetV2 and MobileNetV3Large exported as TFLite.
 * Place the model file at:  app/src/main/assets/pechay_model.tflite
 *
 * Input:  [1, 224, 224, 3] float32 tensor, normalized to [-1.0, 1.0]
 *         (pixel / 127.5) − 1.0  — matches both MobileNetV2 and MobileNetV3Large preprocessing.
 * Output: [1, NUM_CLASSES] float32 softmax probabilities.
 *
 * OOD threshold: predictions with max confidence < 0.70 are flagged as uncertain.
 */
class PechayClassifier(private val context: Context) {

    companion object {
        private const val MODEL_FILE = "pechay_model.tflite"
        private const val INPUT_SIZE = 224
        private const val NUM_CHANNELS = 3
        private const val PIXEL_MEAN = 127.5f
        private const val PIXEL_STD = 127.5f

        /** OOD confidence threshold (must match the value used during training evaluation). */
        const val CONFIDENCE_THRESHOLD = 0.70f

        val CLASS_NAMES = listOf("Healthy", "Nitrogen", "Phosphorus", "Potassium")
        val NUM_CLASSES = CLASS_NAMES.size
    }

    private var interpreter: Interpreter? = null
    private var gpuDelegate: GpuDelegate? = null

    data class ClassificationResult(
        val predictedClass: String,
        val confidence: Float,
        val isConfident: Boolean,
        val allProbabilities: Map<String, Float>
    )

    /**
     * Initialize the TFLite interpreter.
     * Call from a background thread or coroutine.
     * Falls back to CPU if GPU delegate initialization fails.
     */
    fun initialize() {
        val modelBuffer = loadModelFile()

        try {
            gpuDelegate = GpuDelegate()
            val options = Interpreter.Options().apply {
                addDelegate(gpuDelegate!!)
                setNumThreads(4)
            }
            interpreter = Interpreter(modelBuffer, options)
        } catch (e: Exception) {
            // GPU delegate not supported on this device — fall back to CPU
            gpuDelegate?.close()
            gpuDelegate = null
            val options = Interpreter.Options().apply {
                setNumThreads(4)
            }
            interpreter = Interpreter(modelBuffer, options)
        }
    }

    /**
     * Run inference on a Bitmap image.
     * @param bitmap Source image (any size; resized internally to 224×224).
     * @return ClassificationResult with class, confidence, and all probabilities.
     */
    fun classify(bitmap: Bitmap): ClassificationResult {
        val interpreter = requireNotNull(interpreter) {
            "Classifier not initialized. Call initialize() first."
        }

        val inputBuffer = preprocessBitmap(bitmap)
        val outputBuffer = Array(1) { FloatArray(NUM_CLASSES) }

        interpreter.run(inputBuffer, outputBuffer)

        val probabilities = outputBuffer[0]
        val maxIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: 0
        val maxConfidence = probabilities[maxIndex]

        val probabilityMap = CLASS_NAMES.zip(probabilities.toList()).toMap()

        return ClassificationResult(
            predictedClass = CLASS_NAMES[maxIndex],
            confidence = maxConfidence,
            isConfident = maxConfidence >= CONFIDENCE_THRESHOLD,
            allProbabilities = probabilityMap
        )
    }

    /**
     * Classify from a content URI (camera/gallery).
     * Handles EXIF orientation automatically.
     */
    fun classifyFromUri(uri: Uri): ClassificationResult {
        val bitmap = loadAndCorrectBitmapFromUri(uri)
        return classify(bitmap)
    }

    /**
     * Preprocess a Bitmap into the model's expected input format.
     * Resize to 224×224, then apply (pixel / 127.5) − 1.0 normalization.
     */
    private fun preprocessBitmap(bitmap: Bitmap): ByteBuffer {
        val resized = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, true)
        val byteBuffer = ByteBuffer.allocateDirect(
            4 * INPUT_SIZE * INPUT_SIZE * NUM_CHANNELS
        ).apply {
            order(ByteOrder.nativeOrder())
        }

        val pixels = IntArray(INPUT_SIZE * INPUT_SIZE)
        resized.getPixels(pixels, 0, INPUT_SIZE, 0, 0, INPUT_SIZE, INPUT_SIZE)

        for (pixel in pixels) {
            val r = ((pixel shr 16) and 0xFF).toFloat()
            val g = ((pixel shr 8) and 0xFF).toFloat()
            val b = (pixel and 0xFF).toFloat()
            byteBuffer.putFloat((r - PIXEL_MEAN) / PIXEL_STD)
            byteBuffer.putFloat((g - PIXEL_MEAN) / PIXEL_STD)
            byteBuffer.putFloat((b - PIXEL_MEAN) / PIXEL_STD)
        }

        return byteBuffer
    }

    /**
     * Load the TFLite model from the assets folder using memory-mapped I/O.
     * Memory mapping avoids copying the model into RAM and speeds up startup.
     */
    @Throws(IOException::class)
    private fun loadModelFile(): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(MODEL_FILE)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val channel = inputStream.channel
        return channel.map(
            FileChannel.MapMode.READ_ONLY,
            fileDescriptor.startOffset,
            fileDescriptor.declaredLength
        )
    }

    /**
     * Load a Bitmap from a URI and correct its rotation using EXIF metadata.
     */
    private fun loadAndCorrectBitmapFromUri(uri: Uri): Bitmap {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IOException("Cannot open image URI: $uri")

        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()

        val exifStream = context.contentResolver.openInputStream(uri) ?: return bitmap
        val exif = ExifInterface(exifStream)
        exifStream.close()

        val rotation = when (
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        ) {
            ExifInterface.ORIENTATION_ROTATE_90  -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }

        return if (rotation != 0f) {
            val matrix = android.graphics.Matrix().apply { postRotate(rotation) }
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } else {
            bitmap
        }
    }

    /**
     * Release resources. Call when the classifier is no longer needed.
     */
    fun close() {
        interpreter?.close()
        interpreter = null
        gpuDelegate?.close()
        gpuDelegate = null
    }
}
