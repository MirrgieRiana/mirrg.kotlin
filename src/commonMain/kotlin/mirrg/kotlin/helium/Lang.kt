@file:Suppress("unused", "SpellCheckingInspection")
@file:OptIn(ExperimentalContracts::class)

package mirrg.kotlin.helium

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

inline fun unit(block: () -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    block()
}

inline fun <reified O : Any> Any?.castOr(default: () -> O): O {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return this as? O ?: default()
}

inline fun <reified O : Any> Any?.castOrThrow(): O {
    return this as O
}

inline fun <reified O : Any> Any?.castOrNull(): O? {
    return this as? O
}

inline fun <S> S?.or(default: () -> S): S {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return this ?: default()
}

inline fun <O> eval(block: () -> O): O {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return block()
}

fun Boolean.toUnitOrNull() = if (this) Unit else null

data class Slot<T>(var value: T) {
    override fun toString() = "($value)"
}

data class Single<out A>(val first: A) {
    override fun toString() = "($first)"
}
