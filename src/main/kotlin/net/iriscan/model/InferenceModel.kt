package net.iriscan.model

import net.iriscan.inference.DefaultPredictor
import net.iriscan.inference.Predictor
import net.iriscan.translator.Translator

/**
 * @author Anton Kurinnoy
 */
interface InferenceModel<I, O> {
    fun newPredictor(): Predictor<I, O>
}

class DefaultInferenceModel<I, O>(model: ByteArray, translator: Translator<I, O>, processor: PROCESSOR_TYPE) :
    InferenceModel<I, O> {

    private var model: ByteArray
    private var translator: Translator<I, O>
    private var processor: PROCESSOR_TYPE

    init {
        this.model = model
        this.translator = translator
        this.processor = processor
    }

    override fun newPredictor(): Predictor<I, O> = DefaultPredictor(this.model, this.translator, this.processor)
}

class InferenceModelBuilder<I, O> {
    private lateinit var model: ByteArray
    private lateinit var translator: Translator<I, O>
    private lateinit var processor: PROCESSOR_TYPE

    fun setModel(model: ByteArray): InferenceModelBuilder<I, O> {
        this.model = model
        return this
    }

    fun setTranslator(translator: Translator<I, O>): InferenceModelBuilder<I, O> {
        this.translator = translator
        return this
    }

    fun setProcessor(processor: PROCESSOR_TYPE): InferenceModelBuilder<I, O> {
        this.processor = processor
        return this
    }

    fun build(): DefaultInferenceModel<I, O> {
        return DefaultInferenceModel(this.model, this.translator, this.processor)
    }

    companion object {
        fun <I, O> newBuilder(): InferenceModelBuilder<I, O> {
            return InferenceModelBuilder()
        }
    }
}