@file:Suppress("unused", "SpellCheckingInspection")

package mirrg.kotlin.helium.converter


class Converter<T, C>(val encoder: (T) -> C, val decoder: (C) -> T)

fun <T, C> Converter<T, C>.encode(value: T) = this.encoder(value)
fun <T, C> Converter<T, C>.decode(code: C) = this.decoder(code)

val <T, C> Converter<T, C>.reverse get() = Converter(decoder, encoder)

fun <T, C1, C2> Converter<T, C1>.map(encoder2: (C1) -> C2, decoder2: (C2) -> C1): Converter<T, C2> = Converter({ encoder2(this.encode(it)) }, { this.decode(decoder2(it)) })
fun <T, C1, C2> Converter<T, C1>.map(converter: Converter<C1, C2>) = this.map(converter.encoder, converter.decoder)
operator fun <T, C1, C2> Converter<T, C1>.times(converter: Converter<C1, C2>) = this.map(converter)


object Converters {
    fun <T, C> fixed(value: T, code: C) = Converter<T, C>({ code }, { value })
    fun <T> identity() = Converter<T, T>({ it }, { it })

    val INT_TO_STRING = Converter<Int, String>({ it.toString() }, { it.toInt() })
    val STRING_TO_BYTE_ARRAY = Converter<String, ByteArray>({ it.encodeToByteArray() }, { it.decodeToString() })
}
