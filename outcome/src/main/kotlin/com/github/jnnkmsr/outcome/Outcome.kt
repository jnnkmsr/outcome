package com.github.jnnkmsr.outcome

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * An [Outcome] encapsulates either a successful return [value][Success.value]
 * of type [V] or a failure reporting its [cause][Failure.cause] of type [C].
 */
public sealed interface Outcome<out V, out C>

/**
 * A successful [Outcome] encapsulating a [value] of type [V].
 *
 * @property value The returned value of a successful operation.
 */
@JvmInline
public value class Success<out V>(public val value: V) : Outcome<V, Nothing>

/**
 * A failed [Outcome] providing a type-safe [cause] of type [C].
 *
 * @property cause Marks the cause of the failure.
 * @property exception Optional [Exception] causing the failure.
 * @property message Optional callback providing a descriptive message for
 *   logging or reporting this failure.
 */
public data class Failure<out C>(
    public val cause: C,
    public val exception: Exception? = null,
    public val message: (() -> String?)? = null,
) : Outcome<Nothing, C>

/**
 * Builds an [Outcome] by invoking the given [block] and wrapping its returned
 * value into a [Success] instance. If an [Exception] is thrown within [block],
 * it will be caught and converted into a [Failure] by invoking [catch].
 *
 * This function is safe to be called from coroutines. [CancellationException]s,
 * thrown to cancel a coroutine, will be rethrown.
 *
 * Note that as opposed to _[Exception]s_, _[Error]s_ will not be caught by the
 * [catch] block, but will be rethrown.
 *
 * @param catch Callback that will be invoked to convert any [Exception] thrown
 *   within the given [block] into a [Failure] instance.
 * @param block Callback returned a value that will be wrapped into a [Success]
 *   instance when the block returns without throwing any [Exception].
 */
public inline fun <V, C> Outcome(
    catch: (Exception) -> Failure<C>,
    block: () -> V,
): Outcome<V, C> = try {
    Success(block())
} catch (t: Throwable) {
    t.catchAsFailure(catch)
}

/**
 * Converts `this` [Flow] into a flow of [Outcome]s by encapsulating upstream
 * values into [Success] instances and converting upstream [Exception]s into
 * [Failure]s by invoking the given [catch] block.
 *
 * Upstream [CancellationException]s, thrown to cancel [Flow] collection, will
 * be safely rethrown downstream.
 *
 * Note that as opposed to _[Exception]s_, upstream _[Error]s_ will not be
 * caught by the [catch] block, but will be rethrown downstream.
 *
 * @param catch Callback that will be invoked to convert any [Exception] thrown
 *   upstream into a [Failure] instance.
 */
public inline fun <V, C> Flow<V>.outcome(
    crossinline catch: (Exception) -> Failure<C>,
): Flow<Outcome<V, C>> = this
    .map { value -> Success(value) }
    .catch { throwable -> throwable.catchAsFailure(catch) }

/**
 * Helper function to safely catch any uncaught [Exception] within coroutines.
 * Rethrows [CancellationException]s and [Error]s while converting [Exception]s
 * into [Failure]s by invoking the given [catch] block.
 */
@PublishedApi
internal inline fun <C> Throwable.catchAsFailure(
    catch: (Exception) -> Failure<C>,
): Failure<C> = when (this) {
    is CancellationException -> throw this
    is Exception -> catch(this)
    else -> throw this
}
