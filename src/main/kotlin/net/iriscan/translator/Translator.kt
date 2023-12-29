package net.iriscan.translator

import net.iriscan.transform.Transform
import java.awt.image.BufferedImage
import java.util.*

/**
 * @author Anton Kurinnoy
 */
interface Translator<I, O> : PreProcessor<I>, PostProcessor<O>{
    val pipeline: LinkedList<Transform>
}

abstract class BaseTranslator<I, O>(override val pipeline: LinkedList<Transform>) : Translator<I, O>

abstract class ImageTranslator<O>(pipeline: LinkedList<Transform>) : BaseTranslator<BufferedImage, O>(pipeline)