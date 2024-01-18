package net.iriscan.processor.onnx

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import net.iriscan.processor.Processor
import net.iriscan.tensor.Tensor
import net.iriscan.tensor.TensorType
import java.nio.FloatBuffer
import java.nio.IntBuffer

/**
 * @author Anton Kurinnoy
 */
class OnnxProcessor(model: ByteArray) : Processor {

    private val session: OrtSession = OrtEnvironment.getEnvironment().createSession(model)

    override fun initialize() {
    }

    override fun run(input: Map<Int, Tensor>): Map<String, Tensor> {
        if (input.keys.max() >= session.numInputs) {
            throw IllegalArgumentException("Illegal index:${input.keys.max()}")
        }

        val onnxInputTensors = input.map { (index, inputTensor) ->
            session.inputNames.elementAt(index) to createOnnxTensor(inputTensor)
        }.toMap()

        val results = session.run(onnxInputTensors)

        return results.associate {
            val shape = (it.value as OnnxTensor).info.shape.map { shapeItem -> shapeItem.toInt() }.toIntArray()
            val type = (it.value as OnnxTensor).info.type.name
            val floatBuffer = (it.value as OnnxTensor).floatBuffer
            it.key to Tensor(shape, floatBuffer, TensorType.valueOf(type))
        }
    }

    override fun close() {
        session.close()
    }

    private fun createOnnxTensor(inputTensor: Tensor): OnnxTensor {
        if (!inputTensor.data.hasArray()) {
            throw IllegalArgumentException("Input tensor doesn't have array data")
        }

        return when (inputTensor.type) {
            TensorType.INT -> OnnxTensor.createTensor(
                OrtEnvironment.getEnvironment(),
                inputTensor.data as IntBuffer,
                inputTensor.shape.toOnnxShape()
            )

            TensorType.FLOAT -> {
                OnnxTensor.createTensor(
                    OrtEnvironment.getEnvironment(),
                    inputTensor.data as FloatBuffer,
                    inputTensor.shape.toOnnxShape()
                )
            }
        }
    }
}