package mirrg.kotlin.hydrogen

import org.junit.Assert.assertEquals
import org.junit.Test

class NumberTest {
    @Test
    fun minMaxTest() {
        assertEquals(10, 10 min 20)
        assertEquals(20, 10 max 20)
        assertEquals(10.0 as Any, 10.0 min 20.0)
        assertEquals(20.0 as Any, 10.0 max 20.0)
    }

    @Test
    fun cmpTest() {
        assertEquals(-1, 10 cmp 20)
        assertEquals(0, 10 cmp 10)
        assertEquals(1, 20 cmp 10)
        assertEquals(-1, 10.0 cmp 20.0)
        assertEquals(0, 10.0 cmp 10.0)
        assertEquals(1, 20.0 cmp 10.0)
    }
}
