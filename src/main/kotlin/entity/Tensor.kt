package entity

/**
 * @author Anton Kurinnoy
 */
data class Tensor(
    val type: TensorType,
    val processor: Processor,
    val shape: Array<Int>,
    val data: Any
)


enum class TensorType { FACE_RECOGNITION, FACE_DETECTION, FACE_LIVENESS_SCORE }

enum class Processor { TFLITE, ONNX }