package entity

import java.awt.image.BufferedImage

/**
 * @author Anton Kurinnoy
 */
data class Image(
    val width: Int,
    val height: Int,
    val data: BufferedImage
)