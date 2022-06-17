package mirrg.kotlin.gson.hydrogen

import org.junit.Assert.assertEquals
import org.junit.Test

class GsonTest {
    @Test
    fun conversion() {
        assertEquals("100", "100".toJsonElement().toJson()) // 整数が書ける
        assertEquals("100.123", "100.123".toJsonElement().toJson()) // 小数が書ける
        assertEquals("100.123000", "100.123000".toJsonElement().toJson()) // 小数の表記は維持される
        assertEquals("100.0000000000000000000000000000123", "100.0000000000000000000000000000123".toJsonElement().toJson()) // 小数の精度は維持される
        assertEquals("10.000e+3", "10.000e+3".toJsonElement().toJson()) // 指数が書ける
        assertEquals("10.000000E07", "10.000000E07".toJsonElement().toJson()) // 指数の表記は維持される

        assertEquals(""""abc"""", """"abc"""".toJsonElement().toJson()) // 文字列が書ける
        assertEquals(""""[F]"""", """"[\u0046]"""".toJsonElement().toJson()) // 文字列リテラルの表記は正規化される

        assertEquals("true", "true".toJsonElement().toJson()) // 論理値が書ける

        assertEquals("null", "null".toJsonElement().toJson()) // nullが書ける

        assertEquals("[1,2,3]", "[1,2,3]".toJsonElement().toJson()) // 配列が書ける
        assertEquals("""{"a":"abc","b":"def"}""", """{"a":"abc","b":"def"}""".toJsonElement().toJson()) // オブジェクトが書ける
        assertEquals("[1,2,3]", "  [  1  ,  2  ,  3  ]  ".toJsonElement().toJson()) // 空白は正規化される
    }

    @Test
    fun jsonElement() {
        assertEquals("10", 10.jsonElement.toJson()) // Intをjsonにできる
        assertEquals("12.3456", 12.3456.jsonElement.toJson()) // Doubleをjsonにできる
        assertEquals("12.3456", 12.34560.jsonElement.toJson()) // Doubleリテラルは末尾の0を区別できない
        assertEquals("12.3456", "12.3456".toBigDecimal().jsonElement.toJson()) // BigDecimalをjsonにできる
        assertEquals("12.34560", "12.34560".toBigDecimal().jsonElement.toJson()) // BigDecimalは末尾の0を区別する

        assertEquals(""""abc"""", "abc".jsonElement.toJson()) // 文字列をjsonにできる

        assertEquals("""true""", true.jsonElement.toJson()) // 論理値をjsonにできる

        assertEquals("null", jsonNull.toJson()) // nullをjsonにできる

        assertEquals("[1,2]", jsonArray(1.jsonElement, 2.jsonElement).toJson()) // Listをjsonにできる
        assertEquals("""{"a":1,"b":2}""", jsonObject("a" to 1.jsonElement, "b" to 2.jsonElement).toJson()) // Mapをjsonにできる
    }
}
