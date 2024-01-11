package net.iriscan.translator

import net.iriscan.tensor.Tensor

/**
 * @author Anton Kurinnoy
 */
interface PreProcessor<in I> {
    fun preProcessInput(input: I): Map<Int, Tensor>
}