@file:Suppress("unused")

package mirrg.kotlin.hydrogen

inline fun unit(block: () -> Unit) = block()
inline fun <reified O : Any> Any.castOrThrow() = this as O
inline fun <reified O : Any> Any.castOrNull() = this as? O
inline fun <S, I : S, O : S> I?.or(default: () -> O): S = this ?: default()
