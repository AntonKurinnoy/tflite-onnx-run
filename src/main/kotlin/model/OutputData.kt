package model

/**
 * @author Anton Kurinnoy
 */
data class Output(
    val type: OUTPUT_TYPE,
    val data: OutputData
)

sealed class OutputData

data class FaceNetOutputData(
    val length: String,
    val scoreAlgorithm: SCORE_ALGORITHM
) : OutputData()

data class Model320OutputData(
    val outputOrder: Array<String>,
    val bboxType: BBOX_TYPE
) : OutputData()

data class DeePixBiSOutputData(
    val outputOrder: Array<String>,
    val shape: IntArray,
    val method: String,
    val threshold: Double,
    val classes: Array<String>,
) : OutputData()


enum class SCORE_ALGORITHM { L2NORM, COSNORM }

enum class BBOX_TYPE { BBOX_ULURWH, BBOX_ULURBLBR, BBOX_CXCYWH }

enum class OUTPUT_TYPE { SCORES_BOXES, TEMPLATE, MASK, MASK_SCORE }