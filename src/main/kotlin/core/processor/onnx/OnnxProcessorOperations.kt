package core.processor.onnx

import core.processor.ProcessorOperations
import core.tensor.image.ImageTensor

/**
 * @author Anton Kurinnoy
 */
interface OnnxProcessorOperations : ProcessorOperations {
}

class OnnxProcessorOperationsImpl : OnnxProcessorOperations {
    override fun run(imageTensor: ImageTensor): Map<String, Any> {
        TODO("Not yet implemented")
    }

    override fun getInfo() {
        TODO("Not yet implemented")
    }
}