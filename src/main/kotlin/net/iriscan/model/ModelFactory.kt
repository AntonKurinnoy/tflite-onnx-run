package net.iriscan.model

import net.iriscan.translator.Translator
import java.io.ByteArrayOutputStream
import java.math.BigInteger
import java.net.URL
import java.security.MessageDigest

/**
 * @author Anton Kurinnoy
 */
object ModelFactory {
    fun <I, O> create(
        model: ModelInfo,
        inputClass: Class<I>,
        outputClass: Class<O>,
        translator: Translator<I, O>
    ): DefaultModel<I, O> {

        val loadedModel = try {
            val url = URL(model.url)
            val connection = url.openConnection().getInputStream()

            val outputStream = ByteArrayOutputStream()
            connection.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            outputStream.toByteArray()
        } catch (e: Exception) {
            throw IllegalArgumentException("Error downloading model from url: ${e.message}")
        }

        if (!model.checksum.isNullOrEmpty()) {
            val cm = model.checksumMethod!!.name
            val hash = MessageDigest.getInstance(cm).digest(loadedModel)
            val checksum = BigInteger(1, hash).toString(16)
            if (checksum !== model.checksum) {
                throw SecurityException("Checksum for loaded model isn't equal to provided checksum")
            }
        }

        return DefaultModel.builder<I, O>()
            .setModel(loadedModel)
            .setTypes(inputClass, outputClass)
            .setProcessor(model.processor)
            .setTranslator(translator)
            .build()
    }

    fun <I, O> create(
        model: ByteArray,
        processor: PROCESSOR_TYPE,
        inputClass: Class<I>,
        outputClass: Class<O>,
        translator: Translator<I, O>,
        builder: DefaultModel.Builder<I, O>
    ): DefaultModel<I, O> {
        return builder
            .setModel(model)
            .setTypes(inputClass, outputClass)
            .setProcessor(processor)
            .setTranslator(translator)
            .build()
    }
}