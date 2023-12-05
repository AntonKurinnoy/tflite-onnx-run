package service

import entity.common.Image

/**
 * @author Anton Kurinnoy
 */
interface ImageTransform {
    fun applyTransform(image: Image): Image
}

class NormalizeImage(private val mean: Array<Double>, private val std: Array<Double>) : ImageTransform {
    override fun applyTransform(image: Image): Image {
        TODO("Not yet implemented")
    }
}

class ResizeImage(private val width: Int, private val height: Int) : ImageTransform {
    override fun applyTransform(image: Image): Image {
        TODO("Not yet implemented")
    }
}


class ImageTransformBuilder(private var image: Image) {
    private val transforms: MutableList<ImageTransform> = mutableListOf()

    fun normalize(mean: Array<Double>, std: Array<Double>): ImageTransformBuilder {
        transforms.add(NormalizeImage(mean, std))
        return this
    }

    fun resize(width: Int, height: Int): ImageTransformBuilder {
        transforms.add(ResizeImage(width, height))
        return this
    }

    fun build(): Image {
        transforms.forEach {
            image = it.applyTransform(image)
        }
        return image
    }
}