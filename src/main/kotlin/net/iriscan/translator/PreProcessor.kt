package net.iriscan.translator

import net.iriscan.tensor.Tensor

/**
 * @author Anton Kurinnoy
 */
interface PreProcessor<I> {
    fun preProcessInput(input: I): Map<Int, Tensor>
}