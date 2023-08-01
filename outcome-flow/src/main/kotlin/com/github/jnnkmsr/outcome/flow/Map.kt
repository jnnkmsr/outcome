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
import com.github.jnnkmsr.outcome.dropValue
import com.github.jnnkmsr.outcome.mapValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Maps the encapsulated [value][Success.value]s of an upstream [Success] using
 * the given [transform], while emitting upstream [Failure]s unchanged.
 */
public inline fun <V, R, C> Flow<Outcome<V, C>>.mapValue(
    crossinline transform: suspend (value: V) -> R,
): Flow<Outcome<R, C>> = map { outcome ->
    outcome.mapValue { value -> transform(value) }
}

/**
 * Returns a [Flow] of [Outcome]s with `Unit` [value][Success.value]s, while
 * emitting upstream [Failure]s unchanged.
 */
public fun <V, C> Flow<Outcome<V, C>>.dropValue(): Flow<Outcome<Unit, C>> =
    map { outcome -> outcome.dropValue() }
