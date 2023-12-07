package core.tensor

/**
 * @author Anton Kurinnoy
 */
interface Tensor {
    val type: TensorType
    val shape: Array<Int>
    val data: Any
}

enum class TensorType { IMAGE }