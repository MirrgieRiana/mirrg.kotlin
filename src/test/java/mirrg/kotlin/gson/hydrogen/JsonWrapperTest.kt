package mirrg.kotlin.gson.hydrogen

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal

class JsonWrapperTest {
    @Test
    fun is_() {
        fun JsonWrapper.types(): String {
            val list = mutableListOf<String>()
            if (isUndefined) list += "Undefined"
            if (isArray) list += "Array"
            if (isObject) list += "Object"
            if (isPrimitive) list += "Primitive"
            if (isNumber) list += "Number"
            if (isString) list += "String"
            if (isBoolean) list += "Boolean"
            if (isNull) list += "Null"
            return list.joinToString(",")
        }
        assertEquals("Undefined", null.toJsonWrapper().types())
        assertEquals("Array", "[]".toJsonElement().toJsonWrapper().types())
        assertEquals("Object", "{}".toJsonElement().toJsonWrapper().types())
        assertEquals("Primitive,Number", "123".toJsonElement().toJsonWrapper().types())
        assertEquals("Primitive,String", """"abc"""".toJsonElement().toJsonWrapper().types())
        assertEquals("Primitive,Boolean", "true".toJsonElement().toJsonWrapper().types())
        assertEquals("Null", "null".toJsonElement().toJsonWrapper().types())
    }

    @Test
    fun type() {
        assertEquals("Undefined", (null.toJsonWrapper().type))
        assertEquals("Array", ("[]".toJsonElement().toJsonWrapper().type))
        assertEquals("Object", ("{}".toJsonElement().toJsonWrapper().type))
        assertEquals("Number", ("123".toJsonElement().toJsonWrapper().type))
        assertEquals("String", (""""abc"""".toJsonElement().toJsonWrapper().type))
        assertEquals("Boolean", ("true".toJsonElement().toJsonWrapper().type))
        assertEquals("Null", ("null".toJsonElement().toJsonWrapper().type))
    }

    @Test
    fun orNull() {
        assertEquals(true, (null.toJsonWrapper().orNull == null))
        assertEquals(false, ("[]".toJsonElement().toJsonWrapper().orNull == null))
        assertEquals(false, ("{}".toJsonElement().toJsonWrapper().orNull == null))
        assertEquals(false, ("123".toJsonElement().toJsonWrapper().orNull == null))
        assertEquals(false, (""""abc"""".toJsonElement().toJsonWrapper().orNull == null))
        assertEquals(false, ("true".toJsonElement().toJsonWrapper().orNull == null))
        assertEquals(true, ("null".toJsonElement().toJsonWrapper().orNull == null))
    }

    @Test
    fun cast() {
        fun JsonWrapper.casts(): String {
            fun success(block: () -> Unit) = try {
                block()
                true
            } catch (_: Exception) {
                false
            }

            val list = mutableListOf<String>()

            if (success { asJsonUndefined() }) list += "Undefined"
            if (success { asJsonArray() }) list += "Array"
            if (success { asJsonObject() }) list += "Object"
            if (success { asJsonPrimitive() }) list += "Primitive"
            if (success { asJsonNull() }) list += "Null"

            if (success { asNumber() }) list += "Number"
            if (success { asBigInteger() }) list += "BigInteger"
            if (success { asBigDecimal() }) list += "BigDecimal"
            if (success { asString() }) list += "String"
            if (success { asBoolean() }) list += "Boolean"

            if (success { asList() }) list += "List"
            if (success { asMap() }) list += "Map"
            if (success { asByte() }) list += "Byte"
            if (success { asShort() }) list += "Short"
            if (success { asInt() }) list += "Int"
            if (success { asLong() }) list += "Long"
            if (success { asFloat() }) list += "Float"
            if (success { asDouble() }) list += "Double"

            return list.joinToString(",")
        }

        assertEquals("Undefined", null.toJsonWrapper().casts())
        assertEquals("Array,List", "[]".toJsonElement().toJsonWrapper().casts())
        assertEquals("Object,Map", "{}".toJsonElement().toJsonWrapper().casts())
        assertEquals("Primitive,Number,BigInteger,BigDecimal,Byte,Short,Int,Long,Float,Double", "123".toJsonElement().toJsonWrapper().casts())
        assertEquals("Primitive,String", """"abc"""".toJsonElement().toJsonWrapper().casts())
        assertEquals("Primitive,Boolean", "true".toJsonElement().toJsonWrapper().casts())
        assertEquals("Null", "null".toJsonElement().toJsonWrapper().casts())
    }

    @Test
    fun specialCast() {
        // 数値の数値化は桁落ちが発生する
        assertEquals(100, "100.5".toJsonElement().toJsonWrapper().asInt())
        assertEquals((-1).toByte(), "255".toJsonElement().toJsonWrapper().asByte())

        // BigDecimalでは桁数が維持される
        assertTrue(100.0 == "100.00000000000000000000000000000000000000001".toJsonElement().toJsonWrapper().asDouble())
        assertEquals(BigDecimal("100.00000000000000000000000000000000000000001"), "100.00000000000000000000000000000000000000001".toJsonElement().toJsonWrapper().asBigDecimal())
    }

    @Test
    fun get() {
        assertEquals(100, """{"a":[null,{"b":[{"c":{"d":[null, null, [100]]}}]}]}""".toJsonElement().toJsonWrapper()["a"][1]["b"][0]["c"]["d"][2][0].asInt())
        assertEquals("$.a[1].b[0].c.d[2][0]", """{"a":[null,{"b":[{"c":{"d":[null, null, [100]]}}]}]}""".toJsonElement().toJsonWrapper()["a"][1]["b"][0]["c"]["d"][2][0].path)
    }

    @Test
    fun asNumber() {
        assertEquals("123.456E+789", "123.456E+789".toJsonElement().toJsonWrapper().asNumber().toString())
        assertEquals("123.456000e000789", "123.456000e000789".toJsonElement().toJsonWrapper().asNumber().toString()) // 表記が維持される
    }

    @Test
    fun toString_() {
        assertEquals("undefined", null.toJsonWrapper().toString()) // 未定義値はundefined
        assertEquals("[0,a,[2,3],4]", """ [ 0 , "a" , [ 2 , 3 ] , 4 ] """.toJsonElement().toJsonWrapper().toString()) // 配列はスペースなし , で連結
        assertEquals("{a:[0,{b:{c:4,e:6}}]}", """ { "a" : [ 0 , { "b" : { "c" : 4 , "e" : 6 } } ] } """.toJsonElement().toJsonWrapper().toString()) // オブジェクトは " " 無し、スペースなし , で連結
        assertEquals("abc", """"abc"""".toJsonElement().toJsonWrapper().toString()) // 文字列の " " は取り除く
        assertEquals("123.456000e000789", "123.456000e000789".toJsonElement().toJsonWrapper().toString()) // 数値は表記が維持される
        assertEquals("true", "true".toJsonElement().toJsonWrapper().toString()) // 論理値はそのまま
        assertEquals("null", "null".toJsonElement().toJsonWrapper().toString()) // nullはnull
    }
}
