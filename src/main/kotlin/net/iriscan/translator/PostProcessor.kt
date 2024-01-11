package net.iriscan.translator

import net.iriscan.tensor.Tensor

/**
 * @author Anton Kurinnoy
 */
interface PostProcessor<out O> {
    fun postProcessOutput(output: Map<Int, Tensor>): O
}