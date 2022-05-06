@file:Suppress("unused")

package mirrg.kotlin.hydrogen


// toStringを呼び出さないjoin

fun <T : CharSequence> Iterable<T>.join(separator: CharSequence = ", ") = joinToString(separator)
fun <T : CharSequence> Array<T>.join(separator: CharSequence = ", ") = joinToString(separator)
fun <T : CharSequence> Sequence<T>.join(separator: CharSequence = ", ") = joinToString(separator)

fun <T> Iterable<T>.join(separator: CharSequence = ", ", transform: (T) -> CharSequence) = joinToString(separator, transform = transform)
fun <T> Array<T>.join(separator: CharSequence = ", ", transform: (T) -> CharSequence) = joinToString(separator, transform = transform)
fun <T> Sequence<T>.join(separator: CharSequence = ", ", transform: (T) -> CharSequence) = joinToString(separator, transform = transform)


/** 先頭の文字のみを大文字にします。 */
fun String.toUpperCaseHead() = if (isEmpty()) this else take(1).toUpperCase() + drop(1)

/** 先頭の文字のみを小文字にします。 */
fun String.toLowerCaseHead() = if (isEmpty()) this else take(1).toLowerCase() + drop(1)
