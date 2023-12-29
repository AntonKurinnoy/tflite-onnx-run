package net.iriscan.translator.impl

import net.iriscan.tensor.Tensor
import net.iriscan.transform.Transform
import net.iriscan.translator.ImageTranslator
import java.awt.image.BufferedImage
import java.util.*

/**
 * @author Anton Kurinnoy
 */
class FaceDetectionTranslator<ScoresBoxesOutputData>(pipeline: LinkedList<Transform>) :
    ImageTranslator<ScoresBoxesOutputData>(pipeline) {
    override fun preProcessInput(input: BufferedImage): Map<Int, Tensor> {
        TODO("Not yet implemented")

    }

    override fun postProcessOutput(output: Map<Int, Tensor>): ScoresBoxesOutputData {
        TODO("Not yet implemented")
    }
}