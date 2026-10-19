@file:Suppress("unused", "SpellCheckingInspection")

package mirrg.kotlin.helium.converter


class Converter<C, T>(val decoder: (C) -> T, val encoder: (T) -> C)

fun <C, T> Converter<C, T>.encode(value: T) = this.encoder(value)
fun <C, T> Converter<C, T>.decode(code: C) = this.decoder(code)

val <C, T> Converter<C, T>.reverse get() = Converter(encoder, decoder)

fun <A, B, C> Converter<A, B>.map(decoder2: (B) -> C, encoder2: (C) -> B): Converter<A, C> = Converter({ decoder2(this.decode(it)) }, { this.encode(encoder2(it)) })
fun <A, B, C> Converter<A, B>.map(converter: Converter<B, C>) = this.map(converter.decoder, converter.encoder)
operator fun <A, B, C> Converter<A, B>.times(converter: Converter<B, C>) = this.map(converter)


object Converters {
    fun <C, T> fixed(code: C, value: T) = Converter<C, T>({ value }, { code })
    fun <T> identity() = Converter<T, T>({ it }, { it })

    val STRING_TO_INT = Converter<String, Int>({ it.toInt() }, { it.toString() })
    val BYTE_ARRAY_TO_STRING = Converter<ByteArray, String>({ it.decodeToString() }, { it.encodeToByteArray() })
}
