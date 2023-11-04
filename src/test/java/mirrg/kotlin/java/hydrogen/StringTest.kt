package mirrg.kotlin.java.hydrogen

import org.junit.Assert
import org.junit.Test

class StringTest {
    @Test
    fun encodeAndDecodePercent() {
        fun assert(raw: String, encoded: String, encoder: (String) -> String, decoder: (String) -> String) {
            Assert.assertEquals(encoded, encoder(raw))
            Assert.assertEquals(raw, decoder(encoded))
        }

        fun e1(string: String) = string.encodePercent { it in 'a'..'z' }
        fun d1(string: String) = string.decodePercent()
        assert("Aa1+ %", "A%611+ %25", ::e1, ::d1)
        assert("あ", "あ", ::e1, ::d1)

        fun e2(string: String) = string.encodePercent { true }
        fun d2(string: String) = string.decodePercent()
        assert("Aa1+ %", "%41%61%31%2B%20%25", ::e2, ::d2)
        assert("あ", "%E3%81%82", ::e2, ::d2)

        fun e3(string: String) = string.encodePercent(Charsets.UTF_16BE) { true }
        fun d3(string: String) = string.decodePercent(Charsets.UTF_16BE)
        assert("Aa1+ %", "%00%41%00%61%00%31%00%2B%00%20%00%25", ::e3, ::d3)
        assert("あ", "%30%42", ::e3, ::d3)
    }
}
