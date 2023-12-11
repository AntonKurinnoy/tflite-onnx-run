package core.processor

import core.tensor.Tensor

/**
 * @author Anton Kurinnoy
 */
interface Processor {
    fun initialize()
    fun run(input: Map<Int, Tensor>, output: MutableMap<Int, Tensor>)
}