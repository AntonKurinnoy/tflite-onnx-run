package core.tensor

import java.nio.ByteBuffer

/**
 * @author Anton Kurinnoy
 */
data class Tensor(val shape: Shape, val data: ByteBuffer, val type: TensorType)

typealias Shape = IntArray

enum class TensorType { INT, FLOAT }


