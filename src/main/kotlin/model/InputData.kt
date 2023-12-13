package model

/**
 * @author Anton Kurinnoy
 */
data class Input(
    val type: String,
    val data: InputData
)

data class InputData(
    val order: ORDER_TYPE,
    val shape: IntArray,
    val normalization: Array<String>,
)

enum class ORDER_TYPE { CWH, CHW }