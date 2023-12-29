package net.iriscan.transform

import net.iriscan.tensor.Tensor

/**
 * @author Anton Kurinnoy
 */
interface Transform {
    fun transform(tensor: Tensor): Tensor
}