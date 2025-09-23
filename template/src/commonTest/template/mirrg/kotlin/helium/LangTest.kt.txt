package mirrg.kotlin.helium

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail
import kotlin.time.DurationUnit
import kotlin.time.measureTime

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
        assertEquals(Unit, result)
    }

    @Suppress("unused", "UNUSED_EXPRESSION")
    private fun unitTestMethod1() = unit { 10 }

    @Test
    fun castOrTest() {
        assertEquals(10, (10 as Any).castOr<Number> { 20 })
        assertEquals(20, ("abc" as Any).castOr<Number> { 20 })
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

            @Suppress("UNUSED_VARIABLE", "unused")
            val b: Int = a.or { return@a "20" }
            fail()
        })

        // 異なる型の型推論
        assertEquals(1, run {
            @Suppress("RedundantNullableReturnType")
            val a: String? = "s"

            val b: StringBuilder = StringBuilder().append("sb")

            val c = a.or { b } // c : {CharSequence & java.io.Serializable}

            @Suppress("UNUSED_VARIABLE", "unused")
            val charSequence: CharSequence = c // キャスト不要

            c.length // CharSequenceのメソッドが利用できる
        })

    }

    private class Wrapper<T>(val value: T) // 任意の型を受け取る型引数を持っているかのテスト用

    private fun createWrapper(): Wrapper<Int>? = Wrapper(10) // Any?を受理し、かつreifiedでない場合にのみ渡すことができる
    private fun <T> getValue(wrapper: Wrapper<T>?) = wrapper!!.value // 渡した型が戻り値にも表れることのテスト

    private fun getNullableString(): String? = "10" // 最適化防止

    @Test
    fun evalTest() {

        // 最後の戻り値を返す
        run {

            // レシーバー無し版
            assertEquals(10, getValue(mirrg.kotlin.helium.eval { createWrapper() }))

            // レシーバーあり版
            assertEquals(10, getValue("receiver".eval { createWrapper() }))

            // thisのある環境ではレシーバーのあり無し版が同時に成立するが、無修飾で呼び出すことができる
            assertEquals(10, getValue("receiver".run { eval { createWrapper() } }))

        }

        // 副作用を1回だけ起こす
        assertEquals(10, run {
            var i = 0
            mirrg.kotlin.helium.eval { i += 10 }
            i
        })
        assertEquals(10, run {
            var i = 0
            "receiver".eval { i += 10 }
            i
        })

        // ブロック内が1度だけ実行されることが保証されている
        run {
            val a: Int
            mirrg.kotlin.helium.eval {
                a = 10
            }
            assertEquals(10, a)
        }
        run {
            val a: Int
            "receiver".eval {
                a = 10
            }
            assertEquals(10, a)
        }

        // itを新たに作らない
        assertEquals(10, "10".let { mirrg.kotlin.helium.eval { it.toInt() } })
        assertEquals(10, "10".let { "receiver".eval { it.toInt() } })

        // thisを新たに作らない
        run {
            val nullable: String? = getNullableString()
            nullable.run outer@{
                assertEquals(10, run {
                    this@outer!!
                    //this.toInt() // runを使うとthisの参照先が分離するのでスマートキャストができなくなる
                    this@outer.toInt()
                })
            }
        }
        run {
            val nullable: String? = getNullableString()
            nullable.run outer@{
                assertEquals(10, mirrg.kotlin.helium.eval {
                    this@outer!!
                    this.toInt() // evalは常にthisを作らないため、外側のthisが見える
                })
            }
        }
        run {
            val nullable: String? = getNullableString()
            nullable.run outer@{
                assertEquals(10, "receiver".eval {
                    this@outer!!
                    this.toInt() // evalは常にthisを作らないため、外側のthisが見える
                })
            }
        }

    }
}
