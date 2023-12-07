package core.processor

import core.processor.onnx.OnnxProcessorOperationsImpl
import core.processor.tflite.TfLiteProcessorOperationsImpl

/**
 * @author Anton Kurinnoy
 */
object ProcessorFactory : Processor {
    override fun getInstance(processor: ProcessorType): ProcessorOperations {
        return when (processor) {
            ProcessorType.TFLITE -> TfLiteProcessorOperationsImpl()
            ProcessorType.ONNX -> OnnxProcessorOperationsImpl()
        }
    }
}