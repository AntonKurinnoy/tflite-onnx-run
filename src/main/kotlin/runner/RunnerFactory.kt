package runner

import core.translator.Translator
import model.Model
import java.io.ByteArrayOutputStream
import java.math.BigInteger
import java.net.URL
import java.security.MessageDigest


/**
 * @author Anton Kurinnoy
 */
object RunnerFactory {
    fun <I, O> createFromModel(
        model: Model,
        inputClass: Class<I>,
        outputClass: Class<O>,
        translator: Translator<I, O>
    ): Runner<I, O> {

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

        return Runner.builder()
            .setModel(loadedModel)
            .setTypes(inputClass, outputClass)
            .setProcessor(model.processor)
            .setTranslator(translator)
            .build()
    }
}