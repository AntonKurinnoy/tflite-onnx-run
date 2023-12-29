package net.iriscan.model

/**
 * @author Anton Kurinnoy
 */
data class ModelInfo(
    val name: String,
    val type: MODEL_TYPE,
    val processor: PROCESSOR_TYPE,
    val url: String,
    val checksum: String?,
    val checksumMethod: CHECKSUM_METHOD?,
    val input: Input,
    val output: Output
)

sealed class Input(val type: INPUT_TYPE)

data class RGBInputData(
    val order: ORDER_TYPE,
    val shape: IntArray,
    val normalization: Array<String>,
) : Input(type = INPUT_TYPE.RGB)

sealed class Output(val type: OUTPUT_TYPE)

data class MaskScoreOutputData(
    val outputOrder: Array<String>,
    val shape: IntArray,
    val method: String,
    val threshold: Double,
    val classes: Array<String>
) : Output(type = OUTPUT_TYPE.MASK_SCORE)

data class MaskOutputData(
    val outputOrder: Array<String>,
    val shape: IntArray,
    val method: String,
    val threshold: Double,
    val classes: Array<String>
) : Output(type = OUTPUT_TYPE.MASK)

data class TemplateOutputData(
    val length: Int,
    val scoreAlgorithm: SCORE_ALGORITHM,
) : Output(type = OUTPUT_TYPE.TEMPLATE)

data class ScoresBoxesOutputData(
    val outputOrder: Array<String>,
    val bboxType: BBOX_TYPE,
) : Output(type = OUTPUT_TYPE.SCORES_BOXES)


//MODEL ENUM CLASSES
enum class MODEL_TYPE { FACE_LIVENESS_SCORE, FACE_RECOGNITION, FACE_DETECTION }

enum class PROCESSOR_TYPE { ONNX, TFLITE }

enum class CHECKSUM_METHOD { CRC, MD5, SHA256 }


//INPUT ENUM CLASSES
enum class INPUT_TYPE { RGB }

enum class ORDER_TYPE { CWH, CHW }


//OUTPUT ENUM CLASSES
enum class OUTPUT_TYPE { MASK_SCORE, MASK, TEMPLATE, SCORES_BOXES }

enum class SCORE_ALGORITHM { L2NORM, COSNORM }

enum class BBOX_TYPE { BBOX_ULURWH, BBOX_ULURBLBR, BBOX_CXCYWH }