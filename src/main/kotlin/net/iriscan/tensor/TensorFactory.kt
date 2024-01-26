package net.iriscan.tensor

import net.iriscan.model.ORDER_TYPE
import java.awt.Color
import java.awt.image.BufferedImage
import java.nio.FloatBuffer

/**
 * @author Anton Kurinnoy
 */
object TensorFactory {
    fun fromImage(image: BufferedImage, order: ORDER_TYPE): Tensor {
        val width = image.width
        val height = image.height
        val floatArray = FloatArray(3 * width * height)

        return when (order) {
            ORDER_TYPE.CHW -> {
                for (y in 0..<height) {
                    for (x in 0..<width) {
                        val pixel = image.getRGB(x, y)
                        val color = Color(pixel)

                        floatArray[y * width + x] = color.red / 255.0f
                        floatArray[width * height + y * width + x] = color.green / 255.0f
                        floatArray[2 * width * height + y * width + x] = color.blue / 255.0f
                    }
                }
                create(intArrayOf(1, 3, height, width), floatArray)
            }

            ORDER_TYPE.CWH -> {
                for (y in 0 until width) {
                    for (x in 0 until height) {
                        val pixel = image.getRGB(x, y)
                        val color = Color(pixel)

                        floatArray[y * height + x] = color.red / 255.0f
                        floatArray[width * height + y * height + x] = color.green / 255.0f
                        floatArray[2 * width * height + y * height + x] = color.blue / 255.0f
                    }
                }
                create(intArrayOf(1, 3, width, height), floatArray)
            }
        }
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