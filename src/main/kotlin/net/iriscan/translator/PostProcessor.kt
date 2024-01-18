package net.iriscan.translator

import net.iriscan.tensor.Tensor

/**
 * @author Anton Kurinnoy
 */
interface PostProcessor<in I, out O> {
    fun postProcessOutput(input: I, output: Map<String, Tensor>): O
}