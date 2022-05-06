package mirrg.kotlin.hydrogen

import org.junit.Assert.assertEquals
import org.junit.Test

class StringTest {
    @Test
    fun joinTest() {
        assertEquals("a, b, c", listOf("a", "b", "c").asIterable().join())
        assertEquals("a, b, c", listOf("a", "b", "c").toTypedArray().join())
        assertEquals("a, b, c", listOf("a", "b", "c").asSequence().join())
        assertEquals("a|b|c", listOf("a", "b", "c").asIterable().join("|"))
        assertEquals("a|b|c", listOf("a", "b", "c").toTypedArray().join("|"))
        assertEquals("a|b|c", listOf("a", "b", "c").asSequence().join("|"))

        assertEquals("1, 2, 3", listOf(1, 2, 3).asIterable().join { "$it" })
        assertEquals("1, 2, 3", listOf(1, 2, 3).toTypedArray().join { "$it" })
        assertEquals("1, 2, 3", listOf(1, 2, 3).asSequence().join { "$it" })
        assertEquals("1|2|3", listOf(1, 2, 3).asIterable().join("|") { "$it" })
        assertEquals("1|2|3", listOf(1, 2, 3).toTypedArray().join("|") { "$it" })
        assertEquals("1|2|3", listOf(1, 2, 3).asSequence().join("|") { "$it" })
    }

    @Test
    fun to_CaseHeadTest() {
        assertEquals("AbcAbc", "abcAbc".toUpperCaseHead())
        assertEquals("AbcAbc", "AbcAbc".toUpperCaseHead())
        assertEquals("123Abc", "123Abc".toUpperCaseHead())
        assertEquals("A", "a".toUpperCaseHead())
        assertEquals("A", "A".toUpperCaseHead())
        assertEquals("", "".toUpperCaseHead())

        assertEquals("abcAbc", "abcAbc".toLowerCaseHead())
        assertEquals("abcAbc", "AbcAbc".toLowerCaseHead())
        assertEquals("123Abc", "123Abc".toLowerCaseHead())
        assertEquals("a", "a".toLowerCaseHead())
        assertEquals("a", "A".toLowerCaseHead())
        assertEquals("", "".toLowerCaseHead())

    }
}
