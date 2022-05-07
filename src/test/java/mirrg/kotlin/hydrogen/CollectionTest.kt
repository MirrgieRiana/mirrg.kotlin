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
}
