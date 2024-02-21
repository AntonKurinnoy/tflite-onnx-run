package net.iriscan.model

import net.iriscan.processor.onnx.OnnxProcessor
import net.iriscan.processor.tflite.TfLiteProcessor
import net.iriscan.translator.Translator
import net.iriscan.translator.deepixbis.LivenessTranslatorBuilder
import net.iriscan.translator.facenet.FaceRecognitionTranslatorBuilder
import net.iriscan.translator.retinaface.FaceDetectionTranslatorBuilder
import java.io.ByteArrayOutputStream
import java.math.BigInteger
import java.net.URL
import java.security.MessageDigest

/**
 * @author Anton Kurinnoy
 */
object InferenceModelFactory {
    fun <I, O> create(model: ModelInfo): InferenceModel<I, O> {
        val loadedModel = try {
            val url = URL(model.url)
            val connection = url.openConnection().getInputStream()

            val outputStream = ByteArrayOutputStream()
            connection.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            outputStream.toByteArray()
        } catch (e: Exception) {
            throw IllegalArgumentException("Error downloading model from url: ${e.message}")
        }

        if (!model.checksum.isNullOrEmpty()) {
            val cm = model.checksumMethod!!.name
            val hash = MessageDigest.getInstance(cm).digest(loadedModel)
            val checksum = BigInteger(1, hash).toString(16)
            if (checksum !== model.checksum) {
                throw SecurityException("Checksum for loaded model isn't equal to provided checksum")
            }
        }

        val processor = when (model.processor) {
            PROCESSOR_TYPE.ONNX -> OnnxProcessor(loadedModel)
            PROCESSOR_TYPE.TFLITE -> TfLiteProcessor(loadedModel)
        }
        val rgbInputData = model.input as RGBInputData
        val width = rgbInputData.shape.last()
        val height = rgbInputData.shape[rgbInputData.shape.size - 2]
        val meanList = mutableListOf<FloatArray>()
        val stdList = mutableListOf<FloatArray>()
        for (i in 0 until rgbInputData.normalization.size step 2) {
            meanList.add(rgbInputData.normalization[i])
            stdList.add(rgbInputData.normalization[i + 1])
        }

        val translator = when (model.type) {
            MODEL_TYPE.FACE_DETECTION -> {
                val threshold = (model.output as ScoresBoxesOutputData).threshold.toFloat()
                FaceDetectionTranslatorBuilder.newBuilder()
                    .setSize(width, height)
                    .setOrder(rgbInputData.order)
                    .setNormalizeParams(meanList, stdList)
                    .setThreshold(threshold)
                    .build()
            }

            MODEL_TYPE.FACE_RECOGNITION -> FaceRecognitionTranslatorBuilder.newBuilder()
                .setSize(width, height)
                .setOrder(rgbInputData.order)
                .setNormalizeParams(meanList, stdList)
                .build()

            MODEL_TYPE.FACE_LIVENESS_SCORE -> {
                val threshold = (model.output as MaskScoreOutputData).threshold.toFloat()
                val sign = model.output.sign
                LivenessTranslatorBuilder.newBuilder()
                    .setSize(width, height)
                    .setOrder(rgbInputData.order)
                    .setNormalizeParams(meanList, stdList)
                    .setThreshold(threshold)
                    .setSign(sign)
                    .build()
            }
        }

        return InferenceModelBuilder.newBuilder<I, O>()
            .setProcessor(processor)
            .setTranslator(translator as Translator<I, O>)
            .build()
    }

    fun <I, O> create(jsonFilePath: String): InferenceModel<I, O> {
        TODO()
    }
}