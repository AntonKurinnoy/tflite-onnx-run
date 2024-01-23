package net.iriscan.translator.facenet

import net.iriscan.tensor.Tensor
import net.iriscan.transform.Transform
import net.iriscan.translator.ImageBiometricRecognitionTranslator
import net.iriscan.translator.ImageBiometricRecognitionTranslatorBuilder
import net.iriscan.translator.Template
import java.awt.image.BufferedImage

/**
 * @author Anton Kurinnoy
 */
class FaceRecognitionTranslator(
    inputWidth: Int,
    inputHeight: Int,
    meanList: List<FloatArray>,
    stdList: List<FloatArray>,
    transformList: List<Transform>
) : ImageBiometricRecognitionTranslator(inputWidth, inputHeight, meanList, stdList, transformList) {

    override fun postProcessOutput(input: BufferedImage, output: Map<String, Tensor>): Template {
        val template = output.entries.firstOrNull() ?: throw IllegalStateException("Template data is null")
        return template.value.data.rewind().array() as FloatArray
    }
}

class FaceRecognitionTranslatorBuilder : ImageBiometricRecognitionTranslatorBuilder() {

    companion object {
        fun newBuilder(): FaceRecognitionTranslatorBuilder {
            return FaceRecognitionTranslatorBuilder()
        }
    }

    override fun self(): FaceRecognitionTranslatorBuilder {
        return this
    }

    override fun build(): FaceRecognitionTranslator {
        return FaceRecognitionTranslator(inputWidth, inputHeight, meanList, stdList, transformList)
    }
}