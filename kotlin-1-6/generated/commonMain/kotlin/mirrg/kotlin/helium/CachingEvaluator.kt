@file:Suppress("unused", "SpellCheckingInspection")

package mirrg.kotlin.helium

/**
 * 初回の評価結果をキャッシュし、[mirrg.kotlin.helium.CachingEvaluator.reset] で破棄できる遅延評価です。
 * 初期化に失敗した場合は結果を保持せず、次回の評価で再試行します。
 * スレッド間の同期は行いません。
 *
 * @param initializer 評価結果を生成する処理
 */
class CachingEvaluator<T : Any>(private val initializer: () -> T) {

    private var cache: T? = null
    private var evaluating = false

    /** 評価に成功した結果を保持しているかどうかを返します。 */
    val isEvaluated get() = cache != null

    /** 評価を実行せず、キャッシュした結果を返します。未評価の場合は `null` を返します。 */
    fun getOrNull() = cache

    /** 評価を実行せず、キャッシュした結果を返します。未評価の場合は [kotlin.IllegalStateException] をスローします。 */
    fun getOrThrow() = cache ?: throw IllegalStateException("not evaluated")

    /**
     * キャッシュした結果を返し、未評価の場合は [initializer] を実行して結果をキャッシュします。
     * 同じインスタンスの評価中に再び呼び出すと、[kotlin.IllegalStateException] をスローします。
     */
    fun evaluate(): T {
        if (evaluating) throw IllegalStateException("Recursive evaluation of CachingEvaluator")
        val old = cache
        return if (old != null) {
            old
        } else {
            evaluating = true
            try {
                val new = initializer()
                cache = new
                new
            } finally {
                evaluating = false
            }
        }
    }

    /** キャッシュした結果を破棄し、次回の評価時に再計算します。 */
    fun reset() {
        cache = null
    }

}

/**
 * 中断可能な処理による初回の評価結果をキャッシュし、[mirrg.kotlin.helium.SuspendableCachingEvaluator.reset] で破棄できる遅延評価です。
 * 初期化に失敗した場合は結果を保持せず、次回の評価で再試行します。
 * スレッド間の同期は行いません。
 *
 * @param initializer 評価結果を生成する中断可能な処理
 */
class SuspendableCachingEvaluator<T : Any>(private val initializer: suspend () -> T) {

    private var cache: T? = null
    private var evaluating = false

    /** 評価に成功した結果を保持しているかどうかを返します。 */
    val isEvaluated get() = cache != null

    /** 評価を実行せず、キャッシュした結果を返します。未評価の場合は `null` を返します。 */
    fun getOrNull() = cache

    /** 評価を実行せず、キャッシュした結果を返します。未評価の場合は [kotlin.IllegalStateException] をスローします。 */
    fun getOrThrow() = cache ?: throw IllegalStateException("not evaluated")

    /**
     * キャッシュした結果を返し、未評価の場合は [initializer] を実行して結果をキャッシュします。
     * 同じインスタンスの評価中は、中断中も含め、再び呼び出すと [kotlin.IllegalStateException] をスローします。
     */
    suspend fun evaluate(): T {
        if (evaluating) throw IllegalStateException("Recursive evaluation of SuspendableCachingEvaluator")
        val old = cache
        return if (old != null) {
            old
        } else {
            evaluating = true
            try {
                val new = initializer()
                cache = new
                new
            } finally {
                evaluating = false
            }
        }
    }

    /** キャッシュした結果を破棄し、次回の評価時に再計算します。 */
    fun reset() {
        cache = null
    }

}
