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
    private fun method1() = unit { 10 }
}
