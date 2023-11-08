package mirrg.kotlin.hydrogen

import mirrg.kotlin.java.hydrogen.isNotSameAs
import mirrg.kotlin.java.hydrogen.isSameAs
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.math.BigInteger

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

    @Test
    fun sameAs() {
        fun d(string: String) = string.toBigDecimal()
        fun i(string: String) = string.toBigInteger()
        fun assert(isSame: Boolean, a: BigDecimal, b: BigDecimal) {
            assertEquals(isSame, a isSameAs b)
            assertEquals(!isSame, a isNotSameAs b)
        }

        fun assert(isSame: Boolean, a: BigInteger, b: BigInteger) {
            assertEquals(isSame, a isSameAs b)
            assertEquals(!isSame, a isNotSameAs b)
        }
        assert(true, d("1.0"), d("1.0"))
        assert(true, d("1.0"), d("01.0"))
        assert(true, d("1.0"), d("1"))
        assert(true, d("1.0"), d("1.00"))
        assert(false, d("1.0"), d("2.0"))
        assert(true, d("0.0"), d("0"))
        assert(true, d("0.0"), d("0.00"))
        assert(true, d("0.0"), d("-0.0"))

        assert(true, i("1"), i("1"))
        assert(true, i("1"), i("01"))
        assert(false, i("1"), i("2"))
        assert(true, i("0"), i("-0"))
    }
}
