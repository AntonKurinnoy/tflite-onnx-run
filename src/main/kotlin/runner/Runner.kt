package runner

import core.processor.onnx.OnnxProcessor
import core.processor.tflite.TfLiteProcessor
import core.tensor.Tensor
import core.tensor.TensorFactory
import core.translator.Translator
import model.PROCESSOR_TYPE


/**
 * @author Anton Kurinnoy
 */
class Runner<I, O>(builder: Builder<I, O>) {

    private var processor: PROCESSOR_TYPE
    private var model: ByteArray
    private var translator: Translator<I, O>

    init {
        this.processor = builder.processor
        this.model = builder.model
        this.translator = builder.translator
    }

    fun getResult(input: I): O {
        val inputData = translator.preProcessInput(input)

        val processor = when (this.processor) {
            PROCESSOR_TYPE.ONNX -> OnnxProcessor(this.model)
            PROCESSOR_TYPE.TFLITE -> TfLiteProcessor(this.model)
        }
        processor.initialize()

        val outputData = mutableMapOf<Int, Tensor>()
        inputData.forEach { (index, tensor) ->
            val outputTensor = TensorFactory.create(tensor.shape, floatArrayOf())
            outputData[index] = outputTensor
        }
        processor.run(inputData, outputData)

        return translator.postProcessOutput(outputData)
    }

    class Builder<I, O> {
        lateinit var processor: PROCESSOR_TYPE
        private var inputClass: Class<I>? = null
        private var outputClass: Class<O>? = null
        lateinit var model: ByteArray
        lateinit var translator: Translator<I, O>

        internal constructor()

        private constructor(inputClass: Class<I>, outputClass: Class<O>) {
            this.inputClass = inputClass
            this.outputClass = outputClass
        }

        fun <I, O> setTypes(inputClass: Class<I>, outputClass: Class<O>): Builder<I, O> {
            return Builder(inputClass, outputClass)
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