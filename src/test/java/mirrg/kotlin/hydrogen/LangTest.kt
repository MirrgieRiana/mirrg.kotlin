package mirrg.kotlin.hydrogen

import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import java.io.Serializable

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
    fun castOrTest() {
        assertEquals(10, (10 as Any).castOr<Number> { 20 })
        assertThrow<ClassCastException> { ("abc" as Any).castOr<Number> { 20 } }
    }

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

            @Suppress("UNUSED_VARIABLE")
            val b: Int = a.or { return@a "20" }
            fail()
        })

        // 異なる型の型推論
        assertEquals(1, run {
            @Suppress("RedundantNullableReturnType")
            val a: String? = "s"

            val b: StringBuilder = StringBuilder().append("sb")

            val c = a.or { b } // c : {CharSequence & java.io.Serializable}

            @Suppress("UNUSED_VARIABLE")
            val serializable: Serializable = c // キャスト不要

            c.length // CharSequenceのメソッドが利用できる
        })

    }
}
