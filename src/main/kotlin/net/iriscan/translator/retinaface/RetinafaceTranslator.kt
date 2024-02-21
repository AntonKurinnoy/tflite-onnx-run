package net.iriscan.translator.retinaface

import net.iriscan.model.ORDER_TYPE
import net.iriscan.tensor.Tensor
import net.iriscan.transform.Transform
import net.iriscan.translator.Box
import net.iriscan.translator.DetectedBiometric
import net.iriscan.translator.ImageBiometricDetectionTranslator
import net.iriscan.translator.ImageBiometricDetectionTranslatorBuilder
import java.awt.image.BufferedImage

/**
 * @author Anton Kurinnoy
 */
class FaceDetectionTranslator(
    inputWidth: Int,
    inputHeight: Int,
    order: ORDER_TYPE,
    meanList: List<FloatArray>,
    stdList: List<FloatArray>,
    transformList: List<Transform>,
    threshold: Float
) : ImageBiometricDetectionTranslator(inputWidth, inputHeight, order, meanList, stdList, transformList) {

    private val threshold: Float

    init {
        this.threshold = threshold
    }

    override fun postProcessOutput(input: BufferedImage, output: Map<String, Tensor>): List<DetectedBiometric> {
        val startWidth = input.width
        val startHeight = input.height
        val scores = output.entries.firstOrNull() ?: throw IllegalStateException("Scores data is null")
        val boxes = output.entries.lastOrNull() ?: throw IllegalStateException("Boxes data is null")
        val scoresArray = scores.value.data.rewind().array() as FloatArray
        val boxesArray = boxes.value.data.rewind().array() as FloatArray
        val scoresReshaped = splitArrayIntoSubarrays(scoresArray, 2)
        val boxesReshaped = splitArrayIntoSubarrays(boxesArray, 4)
        val (boxesPost, labels, scoresPost) = postprocessResult(
            startWidth,
            startHeight,
            scoresReshaped,
            boxesReshaped,
            threshold.toDouble()
        )

        return boxesPost.mapIndexed { index, it ->
            DetectedBiometric(
                scoresPost[index],
                Box(it[0], it[1], it[2], it[3])
            )
        }.toList()
    }

    private fun postprocessResult(
        width: Int,
        height: Int,
        scores: Array<FloatArray>,
        boxes: Array<FloatArray>,
        threshold: Double
    ): Triple<List<IntArray>, List<Int>, List<Float>> {
        val pickedBoxScores = mutableListOf<List<FloatArray>>()
        val pickedLabels = mutableListOf<Int>()
        (1 until scores.first().size).forEach { classIndex ->
            val probs = scores.map { it[classIndex] }
            val mask = probs.map { it > threshold }
            val filteredProbs = probs.filterIndexed { index, _ -> mask[index] }
            if (filteredProbs.isEmpty()) {
                return@forEach
            }
            val filteredBoxes = boxes.filterIndexed { index, _ -> mask[index] }
            val boxScoresNMS = hardNMS(filteredBoxes, filteredProbs)
            pickedBoxScores.add(boxScoresNMS)
            pickedLabels.addAll(List(boxScoresNMS.size) { classIndex })
        }

        if (pickedBoxScores.isEmpty()) {
            return Triple(listOf(), listOf(), listOf())
        }

        val pickedBoxScoresFlatten = pickedBoxScores.flatten()
        val listOfBoxes = mutableListOf<IntArray>()
        val listOfScores = mutableListOf<Float>()
        pickedBoxScoresFlatten.forEach { box ->
            val rec = intArrayOf(
                (box[0] * width).toInt(),
                (box[1] * height).toInt(),
                (box[2] * width).toInt(),
                (box[3] * height).toInt(),
            )
            listOfBoxes.add(rec)
            listOfScores.add(box[4])
        }


        return Triple(listOfBoxes, pickedLabels.toList(), listOfScores)
    }

    private fun hardNMS(boxes: List<FloatArray>, scores: List<Float>, candidateSize: Int = 200): List<FloatArray> {
        val iouThreshold = 0.3
        val topK = -1
        val picked = mutableListOf<Int>()
        var indexes = scores.indices.sortedBy { scores[it] }.toIntArray()

        while (indexes.size > 0) {
            val current = indexes.last()
            picked.add(current)
            if ((topK > 0 && topK == picked.size) || indexes.size == 1) {
                break
            }
            val currentBox = boxes[current]
            indexes = indexes.dropLast(1).toIntArray()
            val restBoxes = boxes.filterIndexed { index, _ -> index in indexes }
            val iou = calculateIoU(restBoxes, listOf(currentBox))
            indexes = indexes.filterIndexed { index, _ -> iou[index] <= iouThreshold }.toIntArray()
        }

        val reshapedScores = scores.map { listOf(it) }
        return picked.map { boxes[it] }.zip(reshapedScores) { box, score -> box + score }
    }

    private fun calculateIoU(
        restBoxes: List<FloatArray>,
        currentBox: List<FloatArray>,
        eps: Double = 1e-5
    ): List<Float> {
        val value1 = restBoxes.map { floatArrayOf(it[0], it[1]) }
        val value2 = currentBox.map { floatArrayOf(it[0], it[1]) }
        val overlapLeftTop = mutableListOf<FloatArray>()
        restBoxes.indices.forEach {
            val first = maxOf(value1[it][0], value2[0][0])
            val second = maxOf(value1[it][1], value2[0][1])
            overlapLeftTop.add(floatArrayOf(first, second))
        }
        val value3 = restBoxes.map { floatArrayOf(it[2], it[3]) }
        val value4 = currentBox.map { floatArrayOf(it[2], it[3]) }
        val overlapRightBottom = mutableListOf<FloatArray>()
        restBoxes.indices.forEach {
            val first = minOf(value3[it][0], value4[0][0])
            val second = minOf(value3[it][1], value4[0][1])
            overlapRightBottom.add(floatArrayOf(first, second))
        }
        val overlapArea = calculateOverlapAreas(overlapLeftTop, overlapRightBottom)
        val area0 = calculateOverlapAreas(value1, value3)
        val area1 = calculateOverlapAreas(value2, value4)

        val result = mutableListOf<Float>()
        overlapArea.indices.forEach {
            val value = overlapArea[it] / (area0[it] + area1[0] - overlapArea[it] + eps)
            result.add(value.toFloat())
        }

        return result
    }

    private fun calculateOverlapAreas(leftTop: List<FloatArray>, rightBottom: List<FloatArray>): List<Float> {
        if (leftTop.size != rightBottom.size) {
            throw IllegalArgumentException("Lists must have the same size")
        }

        val overlapAreas = mutableListOf<Float>()

        for (i in leftTop.indices) {
            val lt = leftTop[i]
            val rb = rightBottom[i]

            val width = maxOf(0.0f, rb[0] - lt[0])
            val height = maxOf(0.0f, rb[1] - lt[1])

            overlapAreas.add(width * height)
        }

        return overlapAreas
    }

    private fun splitArrayIntoSubarrays(array: FloatArray, subarraySize: Int): Array<FloatArray> {
        val result = mutableListOf<FloatArray>()
        var startIndex = 0

        while (startIndex < array.size) {
            val endIndex = startIndex + subarraySize
            val subList = array.slice(startIndex until endIndex)
            result.add(subList.toFloatArray())
            startIndex = endIndex
        }


        return result.toTypedArray()
    }
}

class FaceDetectionTranslatorBuilder : ImageBiometricDetectionTranslatorBuilder() {
    private var threshold: Float = 0.5f

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
        return FaceDetectionTranslator(inputWidth, inputHeight, order, meanList, stdList, transformList, threshold)
    }
}