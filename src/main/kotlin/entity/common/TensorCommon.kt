package entity.common

import enums.Processor
import enums.TensorType

/**
 * @author Anton Kurinnoy
 */
data class Tensor(
    val type: TensorType,
    val processor: Processor,
    val shape: Array<Int>,
    val data: Any
)
