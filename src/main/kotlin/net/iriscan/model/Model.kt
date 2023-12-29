package net.iriscan.model

import net.iriscan.inference.DefaultPredictor
import net.iriscan.inference.Predictor
import net.iriscan.translator.Translator

/**
 * @author Anton Kurinnoy
 */
interface Model<I, O> {
    fun newPredictor(): Predictor<I, O>
}

class DefaultModel<I, O>(builder: Builder<I, O>) : Model<I, O> {

    private var processor: PROCESSOR_TYPE
    private var model: ByteArray
    private var translator: Translator<I, O>

    init {
        this.processor = builder.processor
        this.model = builder.model
        this.translator = builder.translator
    }

    override fun newPredictor(): Predictor<I, O> = DefaultPredictor(model, translator, processor)

    class Builder<I, O> {
        lateinit var processor: PROCESSOR_TYPE
        lateinit var model: ByteArray
        lateinit var translator: Translator<I, O>
        private lateinit var inputClass: Class<I>
        private lateinit var outputClass: Class<O>

        fun setTypes(inputClass: Class<I>, outputClass: Class<O>): Builder<I, O> {
            this.inputClass = inputClass
            this.outputClass = outputClass
            return this
        }

        fun setProcessor(processor: PROCESSOR_TYPE): Builder<I, O> {
            this.processor = processor
            return this
        }

        fun setModel(model: ByteArray): Builder<I, O> {
            this.model = model
            return this
        }

        fun setTranslator(translator: Translator<I, O>): Builder<I, O> {
            this.translator = translator
            return this
        }

        fun build(): DefaultModel<I, O> {
            return DefaultModel(this)
        }
    }

    companion object {
        fun <I, O> builder(): Builder<I, O> {
            return Builder()
        }
    }
}