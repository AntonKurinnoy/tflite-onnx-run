package net.iriscan.processor

import net.iriscan.tensor.Tensor
import java.io.Closeable

/**
 * @author Anton Kurinnoy
 */
interface Processor : Closeable {
    fun initialize()
    fun run(input: Map<Int, Tensor>, output: MutableMap<Int, Tensor>)
}