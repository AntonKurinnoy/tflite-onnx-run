package net.iriscan.translator

import net.iriscan.tensor.Tensor
import net.iriscan.tensor.TensorFactory
import net.iriscan.transform.Normalization
import net.iriscan.transform.ResizeImage
import net.iriscan.transform.Transform
import java.awt.image.BufferedImage
import java.util.*
import kotlin.properties.Delegates

/**
 * @author Anton Kurinnoy
 */
interface Translator<in I, out O> : PreProcessor<I>, PostProcessor<O>

typealias Pipeline = LinkedList<Transform>

abstract class ImageTranslator<O>(
    inputWidth: Int,
    inputHeight: Int,
    meanList: List<FloatArray>,
    stdList: List<FloatArray>,
    customTransformsList: List<Transform>
) : Translator<BufferedImage, O> {

    private val pipeline: Pipeline = Pipeline()

    init {
        addTransform(ResizeImage(inputWidth, inputHeight))
        for (i in 0..meanList.size) {
            addTransform(Normalization(meanList[i], stdList[i]))
        }
        customTransformsList.forEach { addTransform(it) }
    }

    private fun addTransform(item: Transform) {
        pipeline.add(item)
    }

    override fun preProcessInput(input: BufferedImage): Map<Int, Tensor> {
        val inputTensor = TensorFactory.fromImage(input)

        val transformedTensor = pipeline.fold(inputTensor) { acc, transform ->
            transform.transform(acc)
        }

        return mapOf(0 to transformedTensor)
    }

}

abstract class ImageTranslatorBuilder<out OT : ImageTranslator<*>, out OB> {
    protected var inputWidth by Delegates.notNull<Int>()
    protected var inputHeight by Delegates.notNull<Int>()
    protected lateinit var meanList: List<FloatArray>
    protected lateinit var stdList: List<FloatArray>
    protected lateinit var transformList: List<Transform>

    fun setSize(width: Int, height: Int): OB {
        this.inputWidth = width
        this.inputHeight = height
        return self()
    }

    fun setNormalizeParams(meanList: List<FloatArray>, stdList: List<FloatArray>): OB {
        this.meanList = meanList
        this.stdList = stdList
        return self()
    }

    fun addTransforms(items: Pipeline): OB {
        this.transformList = items
        return self()
    }

    protected abstract fun self(): OB

    abstract fun build(): OT
}

data class Box(val x: Int, val y: Int, val width: Int, val height: Int)

typealias Score = Float

data class DetectedBiometric(val score: Score, val box: Box)

typealias Template = FloatArray

abstract class ImageBiometricDetectionTranslator(
    inputWidth: Int,
    inputHeight: Int,
    meanList: List<FloatArray>,
    stdList: List<FloatArray>,
    transforms: List<Transform>
) : ImageTranslator<List<DetectedBiometric>>(inputWidth, inputHeight, meanList, stdList, transforms)

abstract class ImageBiometricDetectionTranslatorBuilder :
    ImageTranslatorBuilder<ImageBiometricDetectionTranslator, ImageBiometricDetectionTranslatorBuilder>() {
    abstract fun setThreshold(threshold: Float): ImageBiometricDetectionTranslatorBuilder
}

abstract class ImageBiometricRecognitionTranslator(
    inputWidth: Int,
    inputHeight: Int,
    meanList: List<FloatArray>,
    stdList: List<FloatArray>,
    transforms: List<Transform>
) : ImageTranslator<Template>(inputWidth, inputHeight, meanList, stdList, transforms)

abstract class ImageBiometricRecognitionTranslatorBuilder :
    ImageTranslatorBuilder<ImageBiometricRecognitionTranslator, ImageBiometricRecognitionTranslatorBuilder>()

abstract class ImageBiometricLivenessTranslator(
    inputWidth: Int,
    inputHeight: Int,
    meanList: List<FloatArray>,
    stdList: List<FloatArray>,
    transforms: List<Transform>
) : ImageTranslator<Boolean>(inputWidth, inputHeight, meanList, stdList, transforms)

abstract class ImageBiometricLivenessTranslatorBuilder :
    ImageTranslatorBuilder<ImageBiometricLivenessTranslator, ImageBiometricLivenessTranslatorBuilder>() {
    abstract fun setThreshold(threshold: Float): ImageBiometricLivenessTranslatorBuilder
    abstract fun setSign(sign: String): ImageBiometricLivenessTranslatorBuilder
}