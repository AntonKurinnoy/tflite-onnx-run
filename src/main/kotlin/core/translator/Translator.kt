package core.translator

/**
 * @author Anton Kurinnoy
 */
interface Translator<I, O> : PreProcessor<I>, PostProcessor<O>

