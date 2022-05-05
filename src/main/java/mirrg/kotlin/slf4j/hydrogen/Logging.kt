@file:Suppress("unused")

package mirrg.kotlin.slf4j.hydrogen

import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun <T : Any> T.getLogger() = getLogger(javaClass)
fun getLogger(clazz: Class<*>): Logger = LoggerFactory.getLogger(clazz)
