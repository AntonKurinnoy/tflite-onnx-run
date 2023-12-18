package core.translator

import core.tensor.Tensor

/**
 * @author Anton Kurinnoy
 */
interface Translator<I, O> : PreProcessor<I>, PostProcessor<O>

class TranslatorImpl<I, O> : Translator<I, O> {
    override fun postProcessOutput(output: Map<Int, Tensor>): O {
        TODO("Not yet implemented")
    }

    override fun preProcessInput(input: I): Map<Int, Tensor> {
        TODO("Not yet implemented")
    }
}