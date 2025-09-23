@file:Suppress("unused", "SpellCheckingInspection")

package mirrg.kotlin.helium

import kotlin.math.ceil
import kotlin.math.floor


fun List<Double>.averageOrNull() = if (isEmpty()) null else this.average()

/** 小数部の末尾の `0` を削除し、小数部がなくなった場合は小数点も削除します。 */
fun String.stripTrailingZeros() = if ("." in this) this.replace("""\.?0+$""".toRegex(), "") else this


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


// 中置比較
infix fun <T : Comparable<T>> T.min(other: T) = if (this <= other) this else other
infix fun <T : Comparable<T>> T.max(other: T) = if (this >= other) this else other
infix fun <T : Comparable<T>> T.cmp(other: T) = compareTo(other)


// 丸め
fun Float.floorToInt() = floor(this).toInt()
fun Float.floorToLong() = floor(this).toLong()
fun Double.floorToInt() = floor(this).toInt()
fun Double.floorToLong() = floor(this).toLong()
fun Float.ceilToInt() = ceil(this).toInt()
fun Float.ceilToLong() = ceil(this).toLong()
fun Double.ceilToInt() = ceil(this).toInt()
fun Double.ceilToLong() = ceil(this).toLong()
