package core.processor

/**
 * @author Anton Kurinnoy
 */
interface Processor {
    fun getInstance(processor: ProcessorType): ProcessorOperations
}

enum class ProcessorType { TFLITE, ONNX }