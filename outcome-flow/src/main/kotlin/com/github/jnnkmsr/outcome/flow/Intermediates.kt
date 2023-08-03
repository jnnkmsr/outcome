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
import com.github.jnnkmsr.outcome.onSuccess
import com.github.jnnkmsr.outcome.Success
import com.github.jnnkmsr.outcome.onFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

/**
 * Invokes the given [block] on each upstream emission of a [Success]. Then
 * emits the unchanged upstream [Outcome] into the downstream [Flow].
 */
public inline fun <V, C> Flow<Outcome<V, C>>.onSuccess(
    crossinline block: suspend (value: V) -> Unit,
): Flow<Outcome<V, C>> = onEach { outcome ->
    outcome.onSuccess { value -> block(value) }
}

/**
 * Invokes the given [block] on each upstream emission of a [Failure]. Then
 * emits the unchanged upstream [Outcome] into the downstream [Flow].
 */
public inline fun <V, C> Flow<Outcome<V, C>>.onFailure(
    crossinline block: suspend (failure: Failure<C>) -> Unit,
): Flow<Outcome<V, C>> = onEach { outcome ->
    outcome.onFailure { failure -> block(failure) }
}
