package net.iriscan.translator.impl

import net.iriscan.tensor.Tensor
import net.iriscan.transform.Transform
import net.iriscan.translator.ImageTranslator
import java.awt.image.BufferedImage
import java.util.*

/**
 * @author Anton Kurinnoy
 */
class OnnxLivenessTranslator<MaskScoreOutputData>(pipeline: LinkedList<Transform>) :
    ImageTranslator<MaskScoreOutputData>(pipeline) {
    override fun preProcessInput(input: BufferedImage): Map<Int, Tensor> {
        TODO("Not yet implemented")
    }

    override fun postProcessOutput(output: Map<Int, Tensor>): MaskScoreOutputData {
        TODO("Not yet implemented")
    }
}

class TfLiteLivenessTranslator<MaskOutputData>(pipeline: LinkedList<Transform>) :
    ImageTranslator<MaskOutputData>(pipeline) {
    override fun preProcessInput(input: BufferedImage): Map<Int, Tensor> {
        TODO("Not yet implemented")
    }

    override fun postProcessOutput(output: Map<Int, Tensor>): MaskOutputData {
        TODO("Not yet implemented")
    }
}