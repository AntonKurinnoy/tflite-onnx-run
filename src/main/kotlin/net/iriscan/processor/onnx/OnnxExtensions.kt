package net.iriscan.processor.onnx

import net.iriscan.tensor.Shape

/**
 * @author Anton Kurinnoy
 */
internal fun Shape.toOnnxShape(): LongArray {
    return this.map { it.toLong() }.toLongArray()
}