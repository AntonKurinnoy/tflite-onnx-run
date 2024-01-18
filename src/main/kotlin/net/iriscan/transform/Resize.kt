package net.iriscan.transform

import net.iriscan.tensor.Tensor
import java.awt.image.BufferedImage

/**
 * @author Anton Kurinnoy
 */
interface Resize : Transform

class ResizeImage(private val width: Int, private val height: Int) : Resize {
    override fun transform(tensor: Tensor): Tensor {
        TODO()
    }

    companion object {
        fun transform(image: BufferedImage, newWidth: Int, newHeight: Int): BufferedImage {
            val resizedImage = BufferedImage(newWidth, newHeight, image.type)
            val g = resizedImage.createGraphics()
            g.drawImage(image, 0, 0, newWidth, newHeight, null)
            g.dispose()

            return resizedImage
        }
    }
}