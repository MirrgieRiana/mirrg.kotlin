package mirrg.kotlin.hydrogen

import org.junit.Assert.assertEquals
import org.junit.Test

class CollectionTest {
    @Test
    fun not_OrNullTest() {
        assertEquals("a", (listOf("a") as Collection<String>).notEmptyOrNull?.join())
        assertEquals("a", listOf("a").asIterable().notNoneOrNull?.join())
        assertEquals("a", listOf("a").toTypedArray().notEmptyOrNull?.join())
        assertEquals("a", listOf("a").asSequence().notNoneOrNull?.join())
        assertEquals(null, (listOf<String>() as Collection<String>).notEmptyOrNull?.join())
        assertEquals(null, listOf<String>().asIterable().notNoneOrNull?.join())
        assertEquals(null, listOf<String>().toTypedArray().notEmptyOrNull?.join())
        assertEquals(null, listOf<String>().asSequence().notNoneOrNull?.join())
    }

    @Test
    fun separate() {
        fun Iterable<Int>.f() = this.join("") { "$it" }

        assertEquals("14243", listOf(1, 2, 3).separate(4).f())

        assertEquals("123", listOf(1, 2, 3).separate().f())
        assertEquals("1452453", listOf(1, 2, 3).separate(4, 5).f())

        assertEquals("142", listOf(1, 2).separate(4).f())
        assertEquals("1", listOf(1).separate(4).f())
        assertEquals("", listOf<Int>().separate(4).f())

        assertEquals("", listOf<Int>().separate().f())
    }

    @Test
    fun concat() {
        fun Iterable<Int>.f() = this.join("") { "$it" }

        assertEquals("1278347856", listOf(listOf(1, 2), listOf(3, 4), listOf(5, 6)).concat(7, 8).f())

        assertEquals("12734756", listOf(listOf(1, 2), listOf(3, 4), listOf(5, 6)).concat(7).f())
        assertEquals("123456", listOf(listOf(1, 2), listOf(3, 4), listOf(5, 6)).concat().f())

        assertEquals("1783785", listOf(listOf(1), listOf(3), listOf(5)).concat(7, 8).f())
        assertEquals("7878", listOf(listOf<Int>(), listOf(), listOf()).concat(7, 8).f())
        assertEquals("12787856", listOf(listOf(1, 2), listOf(), listOf(5, 6)).concat(7, 8).f())

        assertEquals("127834", listOf(listOf(1, 2), listOf(3, 4)).concat(7, 8).f())
        assertEquals("12", listOf(listOf(1, 2)).concat(7, 8).f())
        assertEquals("", listOf<Iterable<Int>>().concat(7, 8).f())
    }
}
