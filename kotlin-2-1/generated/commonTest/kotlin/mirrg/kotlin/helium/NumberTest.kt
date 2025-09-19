package mirrg.kotlin.helium

import kotlin.test.Test
import kotlin.test.assertEquals

class NumberTest {
    @Test
    fun stripTrailingZerosTest() {
        assertEquals("100.1", "100.100".stripTrailingZeros()) // 末尾の余計な 0 を削除する
        assertEquals("100", "100.00".stripTrailingZeros()) // . が余った場合、ついでに削除する
        assertEquals("100", "100".stripTrailingZeros()) // 整数の末尾は削除しない
    }

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
