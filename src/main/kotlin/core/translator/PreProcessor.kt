package core.translator

import core.tensor.Tensor

/**
 * @author Anton Kurinnoy
 */
interface PreProcessor<I> {
    fun preProcessInput(input: I): Map<Int, Tensor>
}