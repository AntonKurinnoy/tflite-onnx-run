package model

/**
 * @author Anton Kurinnoy
 */
data class Model(
    val name: String,
    val type: String,
    val processor: String,
    val url: String,
    val checksum: String,
    val checksumMethod: String,
    val input: Input,
    val output: Output
)
