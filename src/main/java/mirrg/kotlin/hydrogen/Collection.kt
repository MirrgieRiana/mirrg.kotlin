@file:Suppress("unused")

package mirrg.kotlin.hydrogen

val <T : Collection<I>, I> T.notEmptyOrNull get() = takeIf { isNotEmpty() }
val <T : Iterable<I>, I> T.notNoneOrNull get() = takeIf { !none() }
val <I> Array<I>.notEmptyOrNull get() = takeIf { isNotEmpty() }
val <T : Sequence<I>, I> T.notNoneOrNull get() = takeIf { !none() }
