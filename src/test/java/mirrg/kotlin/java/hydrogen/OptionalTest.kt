package mirrg.kotlin.java.hydrogen

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class OptionalTest {
    @Test
    fun orNullTest() {
        assertEquals(10, Optional.ofNullable(10).orNull)
        assertEquals(null, Optional.ofNullable(null as Int?).orNull)
    }
}
