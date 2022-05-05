package mirrg.kotlin.hydrogen

import junit.framework.Assert.assertEquals
import org.junit.Test

class LangTest {
    @Suppress("UNUSED_EXPRESSION")
    @Test
    fun unitTest() {
        val result: Any = unit { 10 }
        assertEquals(Unit.javaClass, result.javaClass)
    }

    @Suppress("unused", "UNUSED_EXPRESSION")
    private fun unitTestMethod1() = unit { 10 }
}
