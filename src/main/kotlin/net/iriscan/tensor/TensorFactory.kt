package net.iriscan.tensor

import java.awt.Color
import java.awt.image.BufferedImage
import java.nio.FloatBuffer

/**
 * @author Anton Kurinnoy
 */
object TensorFactory {
    fun fromImage(image: BufferedImage): Tensor {
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

        return create(intArrayOf(1, 3, height, width), floatArray)
    }

    fun create(shape: Shape, data: Float): Tensor {
        val floatBuffer = FloatBuffer.allocate(1)
        floatBuffer.put(data)
        floatBuffer.rewind()

        return Tensor(shape, floatBuffer, TensorType.FLOAT)
    }

    fun create(shape: Shape, data: FloatArray): Tensor {
        val floatBuffer = FloatBuffer.allocate(data.size)
        floatBuffer.put(data)
        floatBuffer.rewind()

        return Tensor(shape, floatBuffer, TensorType.FLOAT)
    }
}