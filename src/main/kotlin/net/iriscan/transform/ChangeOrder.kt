package net.iriscan.transform

import net.iriscan.model.ORDER_TYPE
import net.iriscan.tensor.Tensor
import net.iriscan.tensor.TensorFactory

/**
 * @author Anton Kurinnoy
 */
interface Order : Transform

class ChangeOrder(private val newOrder: ORDER_TYPE) : Order {
    override fun transform(tensor: Tensor): Tensor {
        return when (newOrder) {
            ORDER_TYPE.CHW -> changeWHtoHW(tensor)
            ORDER_TYPE.CWH -> changeHWtoWH(tensor)
        }
    }

    private fun changeWHtoHW(tensor: Tensor): Tensor {
        val newArray = FloatArray((tensor.data.rewind().array() as FloatArray).size)
        val floatArray = tensor.data.rewind().array() as FloatArray
        val width = tensor.shape[2]
        val height = tensor.shape[3]

        for (y in 0 until height) {
            for (x in 0 until width) {
                newArray[x * height + y] = floatArray[y * width + x]
                newArray[width * height + x * height + y] = floatArray[width * height + y * width + x]
                newArray[2 * width * height + x * height + y] = floatArray[2 * width * height + y * width + x]
            }
        }

        return TensorFactory.create(intArrayOf(tensor.shape[0], tensor.shape[1], height, width), newArray)
    }

    private fun changeHWtoWH(tensor: Tensor): Tensor {
        val newArray = FloatArray((tensor.data.rewind().array() as FloatArray).size)
        val floatArray = tensor.data.rewind().array() as FloatArray
        val height = tensor.shape[2]
        val width = tensor.shape[3]

        for (y in 0 until height) {
            for (x in 0 until width) {
                newArray[y * width + x] = floatArray[x * height + y]
                newArray[width * height + y * width + x] = floatArray[width * height + x * height + y]
                newArray[2 * width * height + y * width + x] = floatArray[2 * width * height + x * height + y]
            }
        }

        return TensorFactory.create(intArrayOf(tensor.shape[0], tensor.shape[1], width, height), newArray)
    }
}