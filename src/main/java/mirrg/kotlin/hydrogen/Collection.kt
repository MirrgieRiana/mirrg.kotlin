/*
 * Copyright 2022 MirrgieRiana
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("unused", "SpellCheckingInspection")

package mirrg.kotlin.hydrogen

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
