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
import com.github.jnnkmsr.outcome.use
import com.github.jnnkmsr.outcome.useAndMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Uses the [value][Success.value] of an upstream [Success] to execute the given
 * [block], emitting the upstream [Success] unchanged or any [Failure] returned
 * by [block], or emits any upstream [Failure].
 */
public inline fun <V, C> Flow<Outcome<V, C>>.use(
    crossinline block: suspend (value: V) -> Outcome<*, C>,
): Flow<Outcome<V, C>> = map { outcome ->
    outcome.use { value -> block(value) }
}

/**
 * Uses the [value][Success.value] of an upstream [Success] to emit the
 * [Outcome] of the given [block] into the downstream [Flow], while emitting
 * upstream [Failure]s unchanged.
 */
public inline fun <V, R, C> Flow<Outcome<V, C>>.useAndMap(
    crossinline block: suspend (value: V) -> Outcome<R, C>,
): Flow<Outcome<R, C>> = map { outcome ->
    outcome.useAndMap { value -> block(value) }
}
