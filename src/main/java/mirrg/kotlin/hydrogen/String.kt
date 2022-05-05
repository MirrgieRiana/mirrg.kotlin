@file:Suppress("unused")

package mirrg.kotlin.hydrogen

/** 先頭の文字のみを大文字にします。 */
fun String.toUpperCaseHead() = if (isEmpty()) this else take(1).toUpperCase() + drop(1)

/** 先頭の文字のみを小文字にします。 */
fun String.toLowerCaseHead() = if (isEmpty()) this else take(1).toLowerCase() + drop(1)
