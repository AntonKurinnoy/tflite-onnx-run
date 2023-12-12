package core.processor.onnx

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import core.processor.Processor
import core.tensor.FloatTensor
import core.tensor.Tensor
import java.nio.FloatBuffer

/**
 * @author Anton Kurinnoy
 */
class OnnxProcessor(private val model: ByteArray) : Processor {

    private lateinit var env: OrtEnvironment
    private lateinit var session: OrtSession

    override fun initialize() {
        env = OrtEnvironment.getEnvironment()
        session = env.createSession(model)
    }

    override fun run(input: Map<Int, Tensor>, output: MutableMap<Int, Tensor>) {
        if (!::session.isInitialized) {
            throw IllegalStateException("Session is not initialized. Call initialize() first.")
        }

        val inputMap = mutableMapOf<String, OnnxTensor>()

        session.inputNames.forEachIndexed { index, name ->
            val inputTensor = OnnxTensor.createTensor(
                env,
                FloatBuffer.wrap((input[index] as FloatTensor).data),
                (input[index] as FloatTensor).shape.map { it.toLong() }.toLongArray()
            )
            inputMap[name] = inputTensor
        }

        val results = session.run(inputMap)

        results.forEachIndexed { index, mutableEntry ->
            output[index] = mutableEntry as Tensor
        }

    }
}