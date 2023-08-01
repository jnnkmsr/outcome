/*
 * Copyright 2023 Jannik Möser
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.jnnkmsr.outcome

import kotlin.coroutines.cancellation.CancellationException

/**
 * Functional interface that can be implemented to convert caught [Exception]s
 * into [Failure]s.
 */
public fun interface ExceptionHandler<out C> {

    /**
     * Receives a caught [exception] and converts it into a [Failure] instance.
     */
    public operator fun invoke(exception: Exception): Failure<C>
}

/**
 * Builds an [Outcome] by invoking the given [block] and wrapping its returned
 * value into a [Success] instance. If an [Exception] is thrown within [block],
 * it will be caught and converted into a [Failure] by
 * [invoking][ExceptionHandler.invoke] the given [handler].
 *
 * This function is safe to be called from coroutines. [CancellationException]s,
 * thrown to cancel a coroutine, will be rethrown.
 *
 * Note that as opposed to _[Exception]s_, _[Error]s_ will not be caught by the
 * [handler], but will be rethrown.
 *
 * @param handler An [ExceptionHandler] be [invoked][ExceptionHandler.invoke] to
 *   convert any [Exception] thrown within the given [block] into a [Failure]
 *   instance.
 * @param block Callback returned a value that will be wrapped into a [Success]
 *   instance when the block returns without throwing any [Exception].
 */
public inline fun <V, C> Outcome(
    handler: ExceptionHandler<C>,
    block: () -> V,
): Outcome<V, C> = Outcome(handler::invoke, block)
