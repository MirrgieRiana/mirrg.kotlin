package mirrg.kotlin.helium

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotSame
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class CachingEvaluatorTest {
    @Test
    fun initialStateTest() {
        var calls = 0
        val evaluator = CachingEvaluator {
            calls++
            Any()
        }

        assertFalse(evaluator.isEvaluated)
        assertNull(evaluator.getOrNull())
        assertFailsWith<IllegalStateException> { evaluator.getOrThrow() }
        evaluator.reset()
        assertFalse(evaluator.isEvaluated)
        assertNull(evaluator.getOrNull())
        assertEquals(0, calls)
    }

    @Test
    fun evaluateTest() {
        var calls = 0
        val evaluator = CachingEvaluator {
            calls++
            Any()
        }

        val result = evaluator.evaluate()
        assertTrue(evaluator.isEvaluated)
        assertSame(result, evaluator.evaluate())
        assertSame(result, evaluator.getOrNull())
        assertSame(result, evaluator.getOrThrow())
        assertEquals(1, calls)
    }

    @Test
    fun resetTest() {
        var calls = 0
        val evaluator = CachingEvaluator {
            calls++
            Any()
        }
        val first = evaluator.evaluate()

        evaluator.reset()
        evaluator.reset()
        assertFalse(evaluator.isEvaluated)
        assertNull(evaluator.getOrNull())
        assertFailsWith<IllegalStateException> { evaluator.getOrThrow() }
        assertEquals(1, calls)

        val second = evaluator.evaluate()
        assertNotSame(first, second)
        assertSame(second, evaluator.evaluate())
        assertEquals(2, calls)
    }

    @Test
    fun failedEvaluationTest() {
        var calls = 0
        val exception = IllegalArgumentException("initialization failed")
        val evaluator = CachingEvaluator {
            calls++
            if (calls == 1) throw exception
            Any()
        }

        assertSame(exception, assertFailsWith<IllegalArgumentException> { evaluator.evaluate() })
        assertFalse(evaluator.isEvaluated)
        assertNull(evaluator.getOrNull())
        assertFailsWith<IllegalStateException> { evaluator.getOrThrow() }

        val result = evaluator.evaluate()
        assertTrue(evaluator.isEvaluated)
        assertSame(result, evaluator.evaluate())
        assertEquals(2, calls)
    }

    @Test
    fun recursiveEvaluationTest() {
        var calls = 0
        lateinit var evaluator: CachingEvaluator<Int>
        evaluator = CachingEvaluator {
            calls++
            if (calls == 1) evaluator.evaluate() else 42
        }

        assertFailsWith<IllegalStateException> { evaluator.evaluate() }
        assertFalse(evaluator.isEvaluated)
        assertNull(evaluator.getOrNull())
        assertEquals(1, calls)

        assertEquals(42, evaluator.evaluate())
        assertEquals(42, evaluator.evaluate())
        assertEquals(2, calls)
    }

    @Test
    fun nestedEvaluatorsTest() {
        var calls = 0
        val inner = CachingEvaluator {
            calls++
            Any()
        }
        val outer = CachingEvaluator { inner.evaluate() }

        assertSame(outer.evaluate(), inner.evaluate())
        assertTrue(outer.isEvaluated)
        assertTrue(inner.isEvaluated)
        assertEquals(1, calls)
    }
}
