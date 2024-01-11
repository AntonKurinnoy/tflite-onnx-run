package net.iriscan.transform

import net.iriscan.tensor.Tensor

/**
 * @author Anton Kurinnoy
 */
interface Resize : Transform

class ResizeImage(private val width: Int, private val height: Int) : Resize {
    override fun transform(tensor: Tensor): Tensor {
        TODO("Not yet implemented")
    }
}