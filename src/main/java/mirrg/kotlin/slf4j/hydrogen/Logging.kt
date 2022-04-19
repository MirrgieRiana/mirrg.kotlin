package mirrg.kotlin.slf4j.hydrogen

import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun <T : Any> T.getLogger(clazz: Class<*>? = null): Logger = LoggerFactory.getLogger(clazz ?: javaClass)
