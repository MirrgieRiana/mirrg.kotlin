package mirrg.kotlin.hydrogen

import org.junit.Assert.assertEquals
import org.junit.Test

class StringTest {
    @Test
    fun not_OrNullTest() {
        assertEquals("a", "a".notEmptyOrNull)
        assertEquals("a", "a".notBlankOrNull)
        assertEquals(" \r\n\t", " \r\n\t".notEmptyOrNull)
        assertEquals(null, " \r\n\t".notBlankOrNull)
        assertEquals(null, "".notEmptyOrNull)
        assertEquals(null, "".notBlankOrNull)
    }

    @Test
    fun indent() {
        assertEquals(" ", "".indent(" ")) // 空文字の場合、先頭にインデントを付与
        assertEquals(" A", "A".indent(" ")) // 不完全な行の場合、先頭にインデントを付与
        assertEquals(" A\n A", "A\nA".indent(" ")) // 複数行の不完全な行の場合、各行にインデントを付与
        assertEquals(" A\n ", "A\n".indent(" ")) // 完全な行の場合、改行の後にもインデントを付与
        assertEquals(" \n ", "\n".indent(" ")) // 完全な行の本文が空でも同様
        assertEquals(" \n A", "\nA".indent(" ")) // 完全な行の後に不完全な行の場合、各行にインデントを付与
    }

    @Test
    fun escapeRegex() {

        // 英数字はエスケープしない
        assertEquals("0123456789", "0123456789".escapeRegex())
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ".escapeRegex())
        assertEquals("abcdefghijklmnopqrstuvwxyz", "abcdefghijklmnopqrstuvwxyz".escapeRegex())

        // マルチバイト文字はエスケープしない
        assertEquals("あ亜（）　㎡", "あ亜（）　㎡".escapeRegex())

        // 半角空白・改行・タブはエスケープしない
        assertEquals(" \r\n\t", " \r\n\t".escapeRegex())

        // エスケープしない記号
        assertEquals("""!"#%&',-/:;<=>@]_`}~""", """!"#%&',-/:;<=>@]_`}~""".escapeRegex())

        // エスケープする記号
        assertEquals("""\$\(\)\*\+\.\?\[\\\^\{\|""", """$()*+.?[\^{|""".escapeRegex())

    }

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

    @Test
    fun to_CamelCaseTest() {
        assertEquals("Abc123abcAbc-abcAbc", "abc_123abc_abc-abc_Abc".toUpperCamelCase())
        assertEquals("abc123abcAbc-abcAbc", "abc_123abc_abc-abc_Abc".toLowerCamelCase())
        assertEquals("Abc123abcAbc-abcAbc", "Abc_123abc_abc-abc_Abc".toUpperCamelCase())
        assertEquals("Abc123abcAbc-abcAbc", "Abc_123abc_abc-abc_Abc".toLowerCamelCase())

        assertEquals("Abc123abcAbc_abcAbc", "abc-123abc-abc_abc-Abc".toUpperCamelCase("-"))
        assertEquals("abc123abcAbc_abcAbc", "abc-123abc-abc_abc-Abc".toLowerCamelCase("-"))
        assertEquals("Abc123abcAbc_abcAbc", "Abc-123abc-abc_abc-Abc".toUpperCamelCase("-"))
        assertEquals("Abc123abcAbc_abcAbc", "Abc-123abc-abc_abc-Abc".toLowerCamelCase("-"))

        assertEquals("Abc.123abc.Abc-abc.Abc", "abc_123abc_abc-abc_Abc".toUpperCamelCase("_", "."))
        assertEquals("abc.123abc.Abc-abc.Abc", "abc_123abc_abc-abc_Abc".toLowerCamelCase("_", "."))
        assertEquals("Abc.123abc.Abc-abc.Abc", "Abc_123abc_abc-abc_Abc".toUpperCamelCase("_", "."))
        assertEquals("Abc.123abc.Abc-abc.Abc", "Abc_123abc_abc-abc_Abc".toLowerCamelCase("_", "."))
    }

    @Test
    fun formatAs_Test() {
        assertEquals("010", 10.toByte().formatAs("%03d"))
        assertEquals("010", 10.toShort().formatAs("%03d"))
        assertEquals("010", 10.formatAs("%03d"))
        assertEquals("010", 10L.formatAs("%03d"))
        assertEquals("010.000", 10.0F.formatAs("%07.3f"))
        assertEquals("010.000", 10.0.formatAs("%07.3f"))

        assertEquals("010", 10.toByte().formatBy("03"))
        assertEquals("010", 10.toShort().formatBy("03"))
        assertEquals("010", 10.formatBy("03"))
        assertEquals("010", 10L.formatBy("03"))
        assertEquals("010.000", 10.0F.formatBy("07.3"))
        assertEquals("010.000", 10.0.formatBy("07.3"))
    }
}
