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

package mirrg.kotlin.java.hydrogen

import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.Temporal

// Conversion
fun Instant(epochSecond: Long, nanoAdjustment: Long): Instant = Instant.ofEpochSecond(epochSecond, nanoAdjustment)
fun String.toInstant(): Instant = Instant.parse(this)
operator fun LocalDate.plus(time: LocalTime): LocalDateTime = LocalDateTime.of(this, time)
operator fun Temporal.minus(other: Temporal): Duration = Duration.between(other, this)

// Property
val Duration.doubleSeconds get() = seconds.toDouble() + nano / 1_000_000_000.0
