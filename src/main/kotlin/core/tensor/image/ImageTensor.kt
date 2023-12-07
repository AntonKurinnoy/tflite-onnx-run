package core.tensor.image

import core.tensor.Tensor

/**
 * @author Anton Kurinnoy
 */
interface ImageTensor : Tensor {
    override val data: FloatArray

    companion object {
        fun normalize() {}
        fun resize(width: Int, height: Int) {}
    }
}