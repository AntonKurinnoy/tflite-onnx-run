package net.iriscan.transform

import net.iriscan.tensor.Tensor

/**
 * @author Anton Kurinnoy
 */
interface Normalize : Transform

class Normalization(private val mean: FloatArray, private val std: FloatArray) : Normalize {
    override fun transform(tensor: Tensor): Tensor {
        TODO("Not yet implemented")
    }
}