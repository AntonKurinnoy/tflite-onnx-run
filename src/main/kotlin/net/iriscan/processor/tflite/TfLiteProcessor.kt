package net.iriscan.processor.tflite

import net.iriscan.processor.Processor
import net.iriscan.tensor.Tensor

/**
 * @author Anton Kurinnoy
 */
class TfLiteProcessor(val model: ByteArray) : Processor {
    override fun initialize() {
        TODO("Not yet implemented")
    }

    override fun run(input: Map<Int, Tensor>, output: MutableMap<Int, Tensor>) {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }
}