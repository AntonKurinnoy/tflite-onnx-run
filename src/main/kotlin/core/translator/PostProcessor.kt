package core.translator

import core.tensor.Tensor

/**
 * @author Anton Kurinnoy
 */
interface PostProcessor<O> {
    fun postProcessOutput(output: Map<Int, Tensor>): O
}