package net.iriscan.translator.deepixbis

import net.iriscan.tensor.Tensor
import net.iriscan.transform.Transform
import net.iriscan.translator.DetectedBiometric
import net.iriscan.translator.ImageBiometricDetectionTranslator
import net.iriscan.translator.ImageBiometricDetectionTranslatorBuilder

/**
 * @author Anton Kurinnoy
 */
class FaceDetectionTranslator(
    inputWidth: Int,
    inputHeight: Int,
    meanList: List<FloatArray>,
    stdList: List<FloatArray>,
    transformList: List<Transform>,
    threshold: Float
) : ImageBiometricDetectionTranslator(inputWidth, inputHeight, meanList, stdList, transformList) {

    private val threshold: Float

    init {
        this.threshold = threshold
    }

    override fun postProcessOutput(output: Map<Int, Tensor>): List<DetectedBiometric> {
        TODO("Not yet implemented")
    }

}

class FaceDetectionTranslatorBuilder : ImageBiometricDetectionTranslatorBuilder() {
    private var threshold: Float = 0.0f//TODO("check")

    companion object {
        fun newBuilder(): FaceDetectionTranslatorBuilder {
            return FaceDetectionTranslatorBuilder()
        }
    }

    override fun setThreshold(threshold: Float): FaceDetectionTranslatorBuilder {
        this.threshold = threshold
        return this
    }

    override fun self(): FaceDetectionTranslatorBuilder {
        return this
    }

    override fun build(): FaceDetectionTranslator {
        return FaceDetectionTranslator(inputWidth, inputHeight, meanList, stdList, transformList, threshold)
    }
}