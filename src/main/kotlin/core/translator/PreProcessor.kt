package core.translator

import core.tensor.Tensor
import core.transform.Transform
import java.util.*

/**
 * @author Anton Kurinnoy
 */
interface PreProcessor<I> {
    fun preProcessInput(input: I, pipeline: LinkedList<Transform>? = null): Map<Int, Tensor>
}