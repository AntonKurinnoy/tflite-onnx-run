package core.tensor

import java.awt.Color
import java.awt.image.BufferedImage
import java.nio.ByteBuffer
import java.nio.ByteOrder

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

        return create(intArrayOf(1, 3, image.width, image.height), floatArray)
    }

    fun create(shape: Shape, data: Float): Tensor {
        val byteBuffer = ByteBuffer.allocate(4)
        byteBuffer.order(ByteOrder.nativeOrder())
        byteBuffer.put(data.toRawBits().toByte())

        byteBuffer.position(0)

        return Tensor(shape, byteBuffer, TensorType.FLOAT)
    }

    fun create(shape: Shape, data: FloatArray): Tensor {
        val byteBuffer = ByteBuffer.allocate(4 * data.size)
        byteBuffer.order(ByteOrder.nativeOrder())

        data.forEach { byteBuffer.put(it.toRawBits().toByte()) }
        byteBuffer.position(0)

        return Tensor(shape, byteBuffer, TensorType.FLOAT)
    }
}