package net.iriscan.translator.facenet

import net.iriscan.tensor.Tensor
import net.iriscan.transform.Transform
import net.iriscan.translator.ImageBiometricRecognitionTranslator
import net.iriscan.translator.ImageBiometricRecognitionTranslatorBuilder
import net.iriscan.translator.Template

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

    override fun postProcessOutput(output: Map<Int, Tensor>): Template {
        TODO("Not yet implemented")
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