package net.iriscan.tensor

import java.nio.Buffer

/**
 * @author Anton Kurinnoy
 */
data class Tensor(val shape: Shape, val data: Buffer, val type: TensorType)

typealias Shape = IntArray

enum class TensorType { INT, FLOAT }


