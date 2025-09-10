@file:Suppress("unused", "SpellCheckingInspection")

package mirrg.kotlin.helium

inline fun unit(block: () -> Unit) = block()
inline fun <reified O : Any> Any?.castOr(default: () -> O) = this as? O ?: default()
inline fun <reified O : Any> Any?.castOrThrow() = this as O
inline fun <reified O : Any> Any?.castOrNull() = this as? O
inline fun <S> S?.or(default: () -> S) = this ?: default()

fun Boolean.toUnitOrNull() = if (this) Unit else null

data class Slot<T>(var value: T) {
    override fun toString() = "($value)"
}

data class Single<out A>(val first: A) {
    override fun toString() = "($first)"
}
