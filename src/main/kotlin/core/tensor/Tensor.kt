package core.tensor

/**
 * @author Anton Kurinnoy
 */
interface Tensor {
    val shape: IntArray
    val data: Any
}

class FloatTensor(override val shape: IntArray, override val data: FloatArray) : Tensor {
    companion object {
        fun create(shape: IntArray): FloatTensor {
            val size = shape.reduce { acc, i -> acc * i }
            val data = FloatArray(size)
            return FloatTensor(shape, data)
        }

        fun create(shape: IntArray, data: FloatArray): FloatTensor {
            return FloatTensor(shape, data)
        }
    }
}


