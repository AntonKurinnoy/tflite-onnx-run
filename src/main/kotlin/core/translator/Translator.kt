package core.translator

import core.tensor.Tensor
import core.transform.Transform
import java.util.*

/**
 * @author Anton Kurinnoy
 */
interface Translator<I, O> : PreProcessor<I>, PostProcessor<O>

class TranslatorImpl<I, O> : Translator<I, O> {
    override fun preProcessInput(input: I, pipeline: LinkedList<Transform>?): Map<Int, Tensor> {
        TODO("Not yet implemented")
    }

    override fun postProcessOutput(output: Map<Int, Tensor>): O {
        TODO("Not yet implemented")
    }
}