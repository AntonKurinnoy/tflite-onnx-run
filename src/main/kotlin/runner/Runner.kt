package runner

import core.processor.onnx.OnnxProcessor
import core.processor.tflite.TfLiteProcessor
import core.tensor.TensorFactory
import core.translator.Translator
import model.Model


/**
 * @author Anton Kurinnoy
 */
class Runner<I, O>(builder: Builder<I, O>) {

    private var inputClass: Class<I>
    private var outputClass: Class<O>
    private var model: ByteArray
    private var input: I
    private var translator: Translator<I, O>
    private var modelParams: Model

    init {
        this.inputClass = builder.inputClass!!
        this.outputClass = builder.outputClass!!
        this.model = builder.model
        this.input = builder.input ?: throw IllegalArgumentException("Input must be set")
        this.translator = builder.translator
        this.modelParams = builder.modelParams
    }

    fun getResult(): O {
        val inputData = translator.preProcessInput(this.input)
        val processor = when (modelParams.processor) {
            "ONNX" -> OnnxProcessor(this.model)
            "TFLITE" -> TfLiteProcessor(this.model)
            else -> throw IllegalArgumentException("Processor type ${modelParams.processor} wasn't found")
        }
        processor.initialize()
        val outputTensor = TensorFactory.create(modelParams.input.data.shape, floatArrayOf())
        val outputData = mutableMapOf(0 to outputTensor)
        processor.run(inputData, outputData)

        return translator.postProcessOutput(outputData)
    }

    class Builder<I, O> {
        var inputClass: Class<I>? = null
        var outputClass: Class<O>? = null
        lateinit var model: ByteArray
        lateinit var translator: Translator<I, O>
        var input: I? = null
        lateinit var modelParams: Model

        internal constructor()

        private constructor(inputClass: Class<I>, outputClass: Class<O>) {
            this.inputClass = inputClass
            this.outputClass = outputClass
        }

        fun <I, O> setInputOutputTypes(inputClass: Class<I>, outputClass: Class<O>): Builder<I, O> {
            return Builder(inputClass, outputClass)
        }

        fun setModel(model: ByteArray): Builder<I, O> {
            this.model = model
            return this
        }

        fun setTranslator(translator: Translator<I, O>): Builder<I, O> {
            this.translator = translator
            return this
        }

        fun setInput(input: I): Builder<I, O> {
            this.input = input
            return this
        }

        fun setModelParams(modelParams: Model): Builder<I, O> {
            this.modelParams = modelParams
            return this
        }

        fun build(): Runner<I, O> {
            return Runner(this)
        }
    }

    companion object {
        fun builder(): Builder<*, *> {
            return Builder<Any, Any>()
        }
    }

}