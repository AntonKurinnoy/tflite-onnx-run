package core.processor.onnx

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import core.processor.Processor
import core.tensor.Tensor

/**
 * @author Anton Kurinnoy
 */
class OnnxProcessor(model: ByteArray) : Processor {

    private val session: OrtSession = OrtEnvironment.getEnvironment().createSession(model)

    override fun initialize() {
        TODO()
    }

    override fun run(input: Map<Int, Tensor>, output: MutableMap<Int, Tensor>) {
        if (input.keys.max() > session.inputNames.size) {
            throw IllegalArgumentException("Illegal index:${input.keys.max()}")
        }

        val onnxInputTensors = mutableMapOf<String, OnnxTensor>()
        input.forEach { (index, inputTensor) ->
            val onnxInputTensor = OnnxTensor.createTensor(
                OrtEnvironment.getEnvironment(),
                inputTensor.data.asFloatBuffer(),
                inputTensor.shape.toOnnxShape()
            )
            onnxInputTensors[session.inputNames.elementAt(index)] = onnxInputTensor
        }

        val results = session.run(onnxInputTensors)

        results.forEachIndexed { index, mutableEntry ->
            val inputTensor = input[index]
            val data = (mutableEntry as OnnxTensor).byteBuffer
            output[index] = Tensor(inputTensor!!.shape, data, inputTensor.type)
        }

    }

    override fun close() {
        session.close()
    }
}