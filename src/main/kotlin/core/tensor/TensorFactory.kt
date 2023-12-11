package core.tensor

import java.awt.Color
import java.awt.image.BufferedImage

/**
 * @author Anton Kurinnoy
 */
object TensorFactory {
    fun fromImage(image: BufferedImage): FloatTensor {
        val width = image.width
        val height = image.height
        val floatArray = FloatArray(3 * width * height)

        for (y in 0..<height) {
            for (x in 0..<width) {
                val pixel = image.getRGB(x, y)
                val color = Color(pixel)

                floatArray[y * width + x] = color.red / 255.0f
                floatArray[width * height + y * width + x] = color.green / 255.0f
                floatArray[2 * width * height + y * width + x] = color.blue / 255.0f
            }
        }
        return FloatTensor(intArrayOf(1,3,image.width, image.height), floatArray)
    }
}