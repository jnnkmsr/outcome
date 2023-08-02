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

package com.github.jnnkmsr.outcome.flow

import com.github.jnnkmsr.outcome.Failure
import com.github.jnnkmsr.outcome.Outcome
import com.github.jnnkmsr.outcome.Success
import com.github.jnnkmsr.outcome.catchAsFailure
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

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
public inline fun <V, C> Flow<V>.asOutcome(
    crossinline catch: suspend (Exception) -> Failure<C>,
): Flow<Outcome<V, C>> = this
    .map { value -> Success(value) }
    .catch { throwable ->
        throwable.catchAsFailure { exception -> catch(exception) }
    }
