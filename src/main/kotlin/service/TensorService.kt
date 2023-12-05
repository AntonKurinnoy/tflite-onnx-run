package service

import entity.common.Tensor
import enums.Output
import enums.Processor

/**
 * @author Anton Kurinnoy
 */
interface TensorService {
    companion object {
        fun run(tensor: Tensor, params: Any): Output {
            return when (tensor.processor) {
                Processor.TFLITE -> runTfLite(tensor, params)
                Processor.ONNX -> runOnnx(tensor, params)
            }
        }

        private fun runTfLite(tensor: Tensor, params: Any): Output {
            TODO("Not yet implemented")
        }
        private fun runOnnx(tensor: Tensor, params: Any): Output {
            TODO("Not yet implemented")
        }
    }
}