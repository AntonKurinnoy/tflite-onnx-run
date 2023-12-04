package ext

import entity.Processor
import entity.Tensor

/**
 * @author Anton Kurinnoy
 */

fun Tensor.run(params: Any): Output {
    return when (this.processor) {
        Processor.TFLITE -> runTflite(this, params)
        Processor.ONNX -> runOnnx(this, params)
    }
}

fun runOnnx(tensor: Tensor, params: Any): Output {
    TODO("Not yet implemented")
}

fun runTflite(tensor: Tensor, params: Any): Output {
    TODO("Not yet implemented")
}

enum class Output { MASK_SCORE, MASK, TEMPLATE, SCORES_BOXES }