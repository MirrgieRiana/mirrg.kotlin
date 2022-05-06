package mirrg.kotlin.hydrogen

import org.junit.Assert.assertEquals
import org.junit.Test

class StringTest {
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
