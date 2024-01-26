package net.iriscan.translator.deepixbis

import net.iriscan.model.ORDER_TYPE
import net.iriscan.tensor.Tensor
import net.iriscan.transform.Transform
import net.iriscan.translator.ImageBiometricLivenessTranslator
import net.iriscan.translator.ImageBiometricLivenessTranslatorBuilder
import java.awt.image.BufferedImage

/**
 * @author Anton Kurinnoy
 */
class LivenessTranslator(
    inputWidth: Int,
    inputHeight: Int,
    order: ORDER_TYPE,
    meanList: List<FloatArray>,
    stdList: List<FloatArray>,
    transformList: List<Transform>,
    threshold: Float,
    sign: String
) :
    ImageBiometricLivenessTranslator(inputWidth, inputHeight, order, meanList, stdList, transformList) {

    private val threshold: Float
    private val sign: String

    init {
        this.threshold = threshold
        this.sign = sign
    }

    override fun postProcessOutput(input: BufferedImage, output: Map<String, Tensor>): Boolean {
        val mask = output.entries.firstOrNull() ?: throw IllegalStateException("Mask data is null")
        val mean = (mask.value.data.rewind().array() as FloatArray).average().toFloat()

        return compareNumbers(mean, threshold, sign)
    }

    private fun compareNumbers(number1: Float, number2: Float, sign: String): Boolean {
        return when (sign) {
            ">" -> number1 > number2
            "<" -> number1 < number2
            ">=" -> number1 >= number2
            "<=" -> number1 <= number2
            "==" -> number1 == number2
            "!=" -> number1 != number2
            else -> throw IllegalArgumentException("Invalid sign: $sign")
        }
    }
}

class LivenessTranslatorBuilder : ImageBiometricLivenessTranslatorBuilder() {
    private var threshold: Float = 0.5f
    private lateinit var sign: String

    companion object {
        fun newBuilder(): LivenessTranslatorBuilder {
            return LivenessTranslatorBuilder()
        }
    }

    override fun setThreshold(threshold: Float): LivenessTranslatorBuilder {
        this.threshold = threshold
        return this
    }

    override fun setSign(sign: String): LivenessTranslatorBuilder {
        this.sign = sign
        return this
    }

    override fun self(): LivenessTranslatorBuilder {
        return this
    }

    override fun build(): LivenessTranslator {
        return LivenessTranslator(inputWidth, inputHeight, order, meanList, stdList, transformList, threshold, sign)
    }
}