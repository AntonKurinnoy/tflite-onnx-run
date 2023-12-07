package core.processor.tflite

import core.processor.ProcessorOperations
import core.tensor.image.ImageTensor

/**
 * @author Anton Kurinnoy
 */
interface TfLiteProcessorOperations : ProcessorOperations {
}

class TfLiteProcessorOperationsImpl : TfLiteProcessorOperations {
    override fun run(imageTensor: ImageTensor): Map<String, Any> {
        TODO("Not yet implemented")
    }

    override fun getInfo() {
        TODO("Not yet implemented")
    }
}