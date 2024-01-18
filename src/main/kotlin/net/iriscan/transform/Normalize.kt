package net.iriscan.transform

import net.iriscan.tensor.Tensor
import java.nio.FloatBuffer

/**
 * @author Anton Kurinnoy
 */
interface Normalize : Transform

class Normalization(private val mean: FloatArray, private val std: FloatArray) : Normalize {
    override fun transform(tensor: Tensor): Tensor {
        val size = (tensor.data as FloatBuffer).remaining()

        for (i in 0 until size step 3) {
            val red: Float = (tensor.data.get(i) - mean[0]) / std[0]
            val green: Float = (tensor.data.get(i + 1) - mean[1]) / std[1]
            val blue: Float = (tensor.data.get(i + 2) - mean[2]) / std[2]

            tensor.data.put(i, red)
            tensor.data.put(i + 1, green)
            tensor.data.put(i + 2, blue)
        }

        return tensor
    }
}