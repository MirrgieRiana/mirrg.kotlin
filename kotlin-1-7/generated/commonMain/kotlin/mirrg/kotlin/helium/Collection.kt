@file:Suppress("unused", "SpellCheckingInspection")

package mirrg.kotlin.helium

val <T : Collection<I>, I> T.notEmptyOrNull get() = takeIf { isNotEmpty() }
val <T : Iterable<I>, I> T.notNoneOrNull get() = takeIf { !none() }
val <I> Array<I>.notEmptyOrNull get() = takeIf { isNotEmpty() }
val <T : Sequence<I>, I> T.notNoneOrNull get() = takeIf { !none() }


fun <T> Iterable<T>.separateWith(separators: Iterable<T>): List<T> {
    val i = this.iterator()
    if (!i.hasNext()) return listOf()
    val left = mutableListOf<T>()
    left += i.next()
    while (i.hasNext()) {
        left += separators
        left += i.next()
    }
    return left
}

fun <T> Iterable<T>.separate(vararg separators: T) = this.separateWith(separators.asIterable())

fun <T> Iterable<Iterable<T>>.concatWith(separators: Iterable<T>): List<T> {
    val i = this.iterator()
    if (!i.hasNext()) return listOf()
    val left = mutableListOf<T>()
    left += i.next()
    while (i.hasNext()) {
        left += separators
        left += i.next()
    }
    return left
}

fun <T> Iterable<Iterable<T>>.concat(vararg separators: T) = this.concatWith(separators.asIterable())
