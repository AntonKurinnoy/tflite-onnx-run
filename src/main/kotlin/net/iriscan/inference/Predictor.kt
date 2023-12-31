package net.iriscan.inference

import net.iriscan.model.PROCESSOR_TYPE
import net.iriscan.processor.Processor
import net.iriscan.processor.onnx.OnnxProcessor
import net.iriscan.processor.tflite.TfLiteProcessor
import net.iriscan.tensor.Tensor
import net.iriscan.tensor.TensorFactory
import net.iriscan.translator.Translator


/**
 * @author Anton Kurinnoy
 */
interface Predictor<I, O> {
    fun predict(input: I): O
}

internal class DefaultPredictor<I, O>(model: ByteArray, translator: Translator<I, O>, processorType: PROCESSOR_TYPE) :
    Predictor<I, O> {
    private val translator: Translator<I, O>
    private val processor: Processor

    init {
        this.translator = translator
        this.processor = when (processorType) {
            PROCESSOR_TYPE.ONNX -> OnnxProcessor(model)
            PROCESSOR_TYPE.TFLITE -> TfLiteProcessor(model)
        }
    }

    override fun predict(input: I): O {
        val inputData = translator.preProcessInput(input)

        val outputData = mutableMapOf<Int, Tensor>()
        inputData.forEach { (index, tensor) ->
            val outputTensor = TensorFactory.create(tensor.shape, floatArrayOf())
            outputData[index] = outputTensor
        }
        processor.run(inputData, outputData)

        return translator.postProcessOutput(outputData)
    }
}
