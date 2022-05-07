@file:Suppress("unused")

package mirrg.kotlin.hydrogen


// 上限・下限
infix fun Byte.atMost(other: Byte) = coerceAtMost(other)
infix fun Byte.atLeast(other: Byte) = coerceAtLeast(other)
infix fun Short.atMost(other: Short) = coerceAtMost(other)
infix fun Short.atLeast(other: Short) = coerceAtLeast(other)
infix fun Int.atMost(other: Int) = coerceAtMost(other)
infix fun Int.atLeast(other: Int) = coerceAtLeast(other)
infix fun Long.atMost(other: Long) = coerceAtMost(other)
infix fun Long.atLeast(other: Long) = coerceAtLeast(other)
infix fun Float.atMost(other: Float) = coerceAtMost(other)
infix fun Float.atLeast(other: Float) = coerceAtLeast(other)
infix fun Double.atMost(other: Double) = coerceAtMost(other)
infix fun Double.atLeast(other: Double) = coerceAtLeast(other)
infix fun <T : Comparable<T>> T.atMost(other: T) = coerceAtMost(other)
infix fun <T : Comparable<T>> T.atLeast(other: T) = coerceAtLeast(other)
