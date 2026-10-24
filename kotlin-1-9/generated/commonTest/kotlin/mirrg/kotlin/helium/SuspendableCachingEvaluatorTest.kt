package mirrg.kotlin.helium

import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.startCoroutine
import kotlin.coroutines.suspendCoroutine
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNotSame
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class SuspendableCachingEvaluatorTest {
    private class EvaluationContinuation<T> : Continuation<T> {
        override val context = EmptyCoroutineContext
        var result: Result<T>? = null

        override fun resumeWith(result: Result<T>) {
            this.result = result
        }

        fun getOrThrow() = assertNotNull(result).getOrThrow()
    }

    private fun <T> runSuspend(block: suspend () -> T): T {
        val completion = EvaluationContinuation<T>()
        block.startCoroutine(completion)
        return completion.getOrThrow()
    }

    @Test
    fun initialStateTest() {
        var calls = 0
        val evaluator = SuspendableCachingEvaluator {
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
        val evaluator = SuspendableCachingEvaluator {
            calls++
            Any()
        }

        val result = runSuspend { evaluator.evaluate() }
        assertTrue(evaluator.isEvaluated)
        assertSame(result, runSuspend { evaluator.evaluate() })
        assertSame(result, evaluator.getOrNull())
        assertSame(result, evaluator.getOrThrow())
        assertEquals(1, calls)
    }

    @Test
    fun resetTest() {
        var calls = 0
        val evaluator = SuspendableCachingEvaluator {
            calls++
            Any()
        }
        val first = runSuspend { evaluator.evaluate() }

        evaluator.reset()
        evaluator.reset()
        assertFalse(evaluator.isEvaluated)
        assertNull(evaluator.getOrNull())
        assertFailsWith<IllegalStateException> { evaluator.getOrThrow() }
        assertEquals(1, calls)

        val second = runSuspend { evaluator.evaluate() }
        assertNotSame(first, second)
        assertSame(second, runSuspend { evaluator.evaluate() })
        assertEquals(2, calls)
    }

    @Test
    fun failedEvaluationTest() {
        var calls = 0
        val exception = IllegalArgumentException("initialization failed")
        val evaluator = SuspendableCachingEvaluator {
            calls++
            if (calls == 1) throw exception
            Any()
        }

        assertSame(exception, assertFailsWith<IllegalArgumentException> { runSuspend { evaluator.evaluate() } })
        assertFalse(evaluator.isEvaluated)
        assertNull(evaluator.getOrNull())
        assertFailsWith<IllegalStateException> { evaluator.getOrThrow() }

        val result = runSuspend { evaluator.evaluate() }
        assertTrue(evaluator.isEvaluated)
        assertSame(result, runSuspend { evaluator.evaluate() })
        assertEquals(2, calls)
    }

    @Test
    fun recursiveEvaluationTest() {
        var calls = 0
        lateinit var evaluator: SuspendableCachingEvaluator<Int>
        evaluator = SuspendableCachingEvaluator {
            calls++
            if (calls == 1) evaluator.evaluate() else 42
        }

        assertFailsWith<IllegalStateException> { runSuspend { evaluator.evaluate() } }
        assertFalse(evaluator.isEvaluated)
        assertNull(evaluator.getOrNull())
        assertEquals(1, calls)

        assertEquals(42, runSuspend { evaluator.evaluate() })
        assertEquals(42, runSuspend { evaluator.evaluate() })
        assertEquals(2, calls)
    }

    @Test
    fun nestedEvaluatorsTest() {
        var calls = 0
        val inner = SuspendableCachingEvaluator {
            calls++
            Any()
        }
        val outer = SuspendableCachingEvaluator { inner.evaluate() }

        assertSame(runSuspend { outer.evaluate() }, runSuspend { inner.evaluate() })
        assertTrue(outer.isEvaluated)
        assertTrue(inner.isEvaluated)
        assertEquals(1, calls)
    }

    @Test
    fun suspensionTest() {
        var calls = 0
        var pending: Continuation<Any>? = null
        val evaluator = SuspendableCachingEvaluator {
            calls++
            suspendCoroutine<Any> { pending = it }
        }
        val block: suspend () -> Any = { evaluator.evaluate() }
        val firstCompletion = EvaluationContinuation<Any>()
        block.startCoroutine(firstCompletion)

        assertNull(firstCompletion.result)
        assertFalse(evaluator.isEvaluated)
        assertNull(evaluator.getOrNull())
        assertFailsWith<IllegalStateException> { evaluator.getOrThrow() }
        assertFailsWith<IllegalStateException> { runSuspend { evaluator.evaluate() } }
        assertEquals(1, calls)

        val first = Any()
        assertNotNull(pending).resume(first)
        assertSame(first, firstCompletion.getOrThrow())
        assertTrue(evaluator.isEvaluated)
        assertSame(first, evaluator.getOrNull())
        assertSame(first, evaluator.getOrThrow())
        assertSame(first, runSuspend { evaluator.evaluate() })
        assertEquals(1, calls)

        evaluator.reset()
        val secondCompletion = EvaluationContinuation<Any>()
        block.startCoroutine(secondCompletion)
        assertNull(secondCompletion.result)
        assertFalse(evaluator.isEvaluated)
        assertEquals(2, calls)

        val second = Any()
        assertNotNull(pending).resume(second)
        assertSame(second, secondCompletion.getOrThrow())
        assertSame(second, runSuspend { evaluator.evaluate() })
        assertEquals(2, calls)
    }

    @Test
    fun suspendedFailureTest() {
        var calls = 0
        var pending: Continuation<Any>? = null
        val evaluator = SuspendableCachingEvaluator {
            calls++
            suspendCoroutine<Any> { pending = it }
        }
        val block: suspend () -> Any = { evaluator.evaluate() }
        val firstCompletion = EvaluationContinuation<Any>()
        block.startCoroutine(firstCompletion)

        val exception = IllegalArgumentException("initialization failed")
        assertNotNull(pending).resumeWithException(exception)
        assertSame(exception, assertFailsWith<IllegalArgumentException> { firstCompletion.getOrThrow() })
        assertFalse(evaluator.isEvaluated)
        assertNull(evaluator.getOrNull())
        assertFailsWith<IllegalStateException> { evaluator.getOrThrow() }

        val secondCompletion = EvaluationContinuation<Any>()
        block.startCoroutine(secondCompletion)
        assertNull(secondCompletion.result)
        assertFalse(evaluator.isEvaluated)
        assertEquals(2, calls)

        val result = Any()
        assertNotNull(pending).resume(result)
        assertSame(result, secondCompletion.getOrThrow())
        assertTrue(evaluator.isEvaluated)
        assertSame(result, runSuspend { evaluator.evaluate() })
        assertEquals(2, calls)
    }
}
