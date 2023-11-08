package mirrg.kotlin.java.hydrogen

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Duration
import java.time.LocalDateTime

class TimeTest {
    @Test
    fun minus() {
        val start = LocalDateTime.of(2000, 1, 1, 0, 0, 0)
        val end = LocalDateTime.of(2000, 1, 1, 0, 0, 10)
        assertEquals(10, (end - start).seconds)
        assertEquals(-10, (start - end).seconds)
    }

    @Test
    fun doubleSeconds() {
        assertEquals(12.345, Duration.ofMillis(12345).doubleSeconds, 0.000001)
    }
}
