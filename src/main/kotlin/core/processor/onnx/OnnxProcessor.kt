package core.processor.onnx

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import core.processor.Processor
import core.tensor.Tensor
import java.nio.ByteBuffer

/**
 * @author Anton Kurinnoy
 */
class OnnxProcessor(model: ByteArray) : Processor {

    private val session: OrtSession = OrtEnvironment.getEnvironment().createSession(model)

    override fun initialize() {
    }

    override fun run(input: Map<Int, Tensor>, output: MutableMap<Int, Tensor>) {
        if (input.keys.max() >= session.numInputs) {
            throw IllegalArgumentException("Illegal index:${input.keys.max()}")
        }

        val onnxInputTensors = input.map { (index, inputTensor) ->
            if (!inputTensor.data.hasArray()) {
                throw IllegalArgumentException("Input tensor doesn't have array data")
            }
            val onnxInputTensor = OnnxTensor.createTensor(
                OrtEnvironment.getEnvironment(),
                inputTensor.data.array() as ByteBuffer,
                inputTensor.shape.toOnnxShape()
            )
            session.inputNames.elementAt(index) to onnxInputTensor
        }.toMap()

        val results = session.run(onnxInputTensors)

        for ((index, value) in results.withIndex()) {
            val inputTensor = input[index]
            val data = (value as OnnxTensor).byteBuffer
            output[index] = Tensor(inputTensor!!.shape, data, inputTensor.type)
        }
    }

    override fun close() {
        session.close()
    }
}