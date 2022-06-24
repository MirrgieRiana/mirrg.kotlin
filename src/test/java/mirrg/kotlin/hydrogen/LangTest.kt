package mirrg.kotlin.hydrogen

import junit.framework.Assert.assertEquals
import junit.framework.Assert.fail
import org.junit.Test

class LangTest {
    private inline fun <reified T : Throwable> assertThrow(block: () -> Any) {
        try {
            block()
            fail()
        } catch (e: Throwable) {
            if (e is T) return
            throw e
        }
    }

    @Suppress("UNUSED_EXPRESSION")
    @Test
    fun unitTest() {
        val result: Any = unit { 10 }
        assertEquals(Unit.javaClass, result.javaClass)
    }

    @Suppress("unused", "UNUSED_EXPRESSION")
    private fun unitTestMethod1() = unit { 10 }

    @Test
    fun castOrThrowTest() {
        assertEquals(10, (10 as Any).castOrThrow<Number>())
        assertThrow<ClassCastException> { ("abc" as Any).castOrThrow<Number>() }
    }

    @Test
    fun castOrNullTest() {
        assertEquals(10, (10 as Any).castOrNull<Number>())
        assertEquals(null, ("abc" as Any).castOrNull<Number>())
    }

    @Test
    fun orTest() {

        // 型が異なるパターン
        assertEquals("10", (10 as Int?).or { 20.0 }.toInt().toString())
        assertEquals("20", (null as Int?).or { 20.0 }.toInt().toString())

        // 右辺がNothingのパターン
        assertEquals(10, run a@{
            @Suppress("RedundantNullableReturnType")
            val a: Int? = 10
            val b: Int = a.or { return@a "20" }
            b
        })
        assertEquals("20", run a@{
            val a: Int? = null
            val b: Int = a.or { return@a "20" }
            fail()
        })

    }
}
