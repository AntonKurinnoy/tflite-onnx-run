package net.iriscan.inference

import net.iriscan.processor.Processor
import net.iriscan.translator.Translator


/**
 * @author Anton Kurinnoy
 */
interface Predictor<in I, out O> {
    fun predict(input: I): O
}

internal class DefaultPredictor<I, O>(processor: Processor, translator: Translator<I, O>) :
    Predictor<I, O> {
    private val translator: Translator<I, O>
    private val processor: Processor

    init {
        this.translator = translator
        this.processor = processor
    }

    override fun predict(input: I): O {
        val inputData = translator.preProcessInput(input)
        val outputData = processor.run(inputData)
        return translator.postProcessOutput(input, outputData)
    }
}
