package core.transform

import core.tensor.Tensor

/**
 * @author Anton Kurinnoy
 */
interface Transform {
    fun transform(tensor: Tensor): Tensor
}