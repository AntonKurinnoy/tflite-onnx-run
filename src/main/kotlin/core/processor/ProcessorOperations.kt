package core.processor

import core.tensor.image.ImageTensor

/**
 * @author Anton Kurinnoy
 */
interface ProcessorOperations {
    fun run(imageTensor: ImageTensor): Map<String, Any>

    fun getInfo()
}