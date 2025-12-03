@file:Suppress("unused", "SpellCheckingInspection")

package mirrg.kotlin.helium


val String.notEmptyOrNull get() = ifEmpty { null }
val String.notBlankOrNull get() = ifBlank { null }

fun String.indent(indent: String) = indent + this.replace("""\r\n|\n|\r""".toRegex()) { it.value + indent }

fun String.escapeRegex() = this.replace("""[.\[^$()*+?{|\\]""".toRegex()) { "\\" + it.groups[0]?.value!! }


// toStringを呼び出さないjoin
// TODO Change default separator to empty string

@Deprecated("Default separator is planned to be changed to empty string in future versions.")
fun <T : CharSequence> Iterable<T>.join(separator: CharSequence = ", ") = joinToString(separator)

@Deprecated("Default separator is planned to be changed to empty string in future versions.")
fun <T : CharSequence> Array<T>.join(separator: CharSequence = ", ") = joinToString(separator)

@Deprecated("Default separator is planned to be changed to empty string in future versions.")
fun <T : CharSequence> Sequence<T>.join(separator: CharSequence = ", ") = joinToString(separator)

@Deprecated("Default separator is planned to be changed to empty string in future versions.")
fun <T> Iterable<T>.join(separator: CharSequence = ", ", transform: (T) -> CharSequence) = joinToString(separator, transform = transform)

@Deprecated("Default separator is planned to be changed to empty string in future versions.")
fun <T> Array<T>.join(separator: CharSequence = ", ", transform: (T) -> CharSequence) = joinToString(separator, transform = transform)

@Deprecated("Default separator is planned to be changed to empty string in future versions.")
fun <T> Sequence<T>.join(separator: CharSequence = ", ", transform: (T) -> CharSequence) = joinToString(separator, transform = transform)


@Deprecated("Removing.")
fun <T : CharSequence> Iterable<T>.join2(separator: CharSequence = "") = joinToString(separator)

@Deprecated("Removing.")
fun <T : CharSequence> Array<T>.join2(separator: CharSequence = "") = joinToString(separator)

@Deprecated("Removing.")
fun <T : CharSequence> Sequence<T>.join2(separator: CharSequence = "") = joinToString(separator)

@Deprecated("Removing.")
fun <T> Iterable<T>.join2(separator: CharSequence = "", transform: (T) -> CharSequence) = joinToString(separator, transform = transform)

@Deprecated("Removing.")
fun <T> Array<T>.join2(separator: CharSequence = "", transform: (T) -> CharSequence) = joinToString(separator, transform = transform)

@Deprecated("Removing.")
fun <T> Sequence<T>.join2(separator: CharSequence = "", transform: (T) -> CharSequence) = joinToString(separator, transform = transform)


/** 先頭の文字のみを大文字にします。 */
fun String.toUpperCaseHead() = if (isEmpty()) this else take(1).uppercase() + drop(1)

/** 先頭の文字のみを小文字にします。 */
fun String.toLowerCaseHead() = if (isEmpty()) this else take(1).lowercase() + drop(1)

/** @receiver スネークケースの文字列 */
fun String.toUpperCamelCase(beforeDelimiter: String = "_", afterDelimiter: String = "") = split(beforeDelimiter).map { it.toUpperCaseHead() }.join(afterDelimiter)

/** @receiver スネークケースの文字列 */
fun String.toLowerCamelCase(beforeDelimiter: String = "_", afterDelimiter: String = "") = split(beforeDelimiter).mapIndexed { i, it -> if (i == 0) it else it.toUpperCaseHead() }.join(afterDelimiter)


// Regex

fun CharSequence.match(regex: Regex) = regex.matchEntire(this)

operator fun MatchResult.get(index: Int) = this.groups[index]?.value
