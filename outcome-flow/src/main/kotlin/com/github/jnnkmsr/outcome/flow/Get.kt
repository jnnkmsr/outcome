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
import com.github.jnnkmsr.outcome.getOrElse
import com.github.jnnkmsr.outcome.getOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import kotlin.experimental.ExperimentalTypeInference

/**
 * Converts `this` [Flow] into a [Flow] of the encapsulated
 * [value][Success.value]s if the upstream [Outcome] is a [Success], or calls
 * [onFailure] to handle upstream [Failure]s.
 *
 * This is the most flexible getter, as [onFailure] may or may not emit values
 * downstream.
 *
 * @see transform
 */
@OptIn(ExperimentalTypeInference::class)
public inline fun <V : R, R, C> Flow<Outcome<V, C>>.get(
    @BuilderInference
    crossinline onFailure: suspend FlowCollector<R>.(failure: Failure<C>) -> Unit = {},
): Flow<R> = transform { outcome ->
    when (outcome) {
        is Success -> emit(outcome.value)
        is Failure -> onFailure(outcome)
    }
}

/**
 * Converts `this` [Flow] into a [Flow] of the encapsulated
 * [value][Success.value]s if the upstream [Outcome] is a [Success], or calls
 * [onFailure] to handle upstream [Failure]s and emitting a replacement value
 * downstream.
 */
public inline fun <V : R, R, C> Flow<Outcome<V, C>>.getOrElse(
    crossinline onFailure: (failure: Failure<C>) -> R,
): Flow<R> = map { outcome -> outcome.getOrElse(onFailure) }

/**
 * Converts `this` [Flow] into a [Flow] of the encapsulated
 * [value][Success.value]s if the upstream [Outcome] is a [Success], or calls
 * [onFailure] to handle upstream [Failure]s and emit `null` downstream.
 */
public inline fun <V, C> Flow<Outcome<V, C>>.getOrNull(
    crossinline onFailure: (failure: Failure<C>) -> Unit = {},
): Flow<V?> = map { outcome -> outcome.getOrNull(onFailure) }
