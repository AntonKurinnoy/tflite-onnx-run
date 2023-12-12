package core.processor.onnx

import core.tensor.Shape

/**
 * @author Anton Kurinnoy
 */
internal fun Shape.toOnnxShape(): LongArray {
    return this.map { it.toLong() }.toLongArray()
}