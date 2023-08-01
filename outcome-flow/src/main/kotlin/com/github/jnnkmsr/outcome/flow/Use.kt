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
import com.github.jnnkmsr.outcome.useOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import kotlin.experimental.ExperimentalTypeInference

/**
 * Converts `this` [Flow] into a [Flow] of the encapsulated
 * [value][Success.value]s if the upstream [Outcome] is a [Success], or calls
 * [onFailure] to handle upstream [Failure]s and emitting a replacement value
 * downstream.
 */
public inline fun <V, C> Flow<Outcome<V, C>>.use(
    crossinline onFailure: (failure: Failure<C>) -> V,
): Flow<V> = map { outcome -> outcome.use(onFailure) }

/**
 * Converts `this` [Flow] into a [Flow] of the encapsulated
 * [value][Success.value]s if the upstream [Outcome] is a [Success], or calls
 * [onFailure] to handle upstream [Failure]s.
 *
 * This is a more flexible overload, as [onFailure] may or may not emit values
 * downstream.
 *
 * @see transform
 */
@OptIn(ExperimentalTypeInference::class)
public inline fun <V, C> Flow<Outcome<V, C>>.use(
    @BuilderInference
    crossinline onFailure: suspend FlowCollector<V>.(failure: Failure<C>) -> Unit,
): Flow<V> = transform { outcome ->
    when (outcome) {
        is Success -> emit(outcome.value)
        is Failure -> onFailure(outcome)
    }
}

/**
 * Converts `this` [Flow] into a [Flow] of values returned by [onSuccess] if
 * the upstream [Outcome] is a [Success], or calls [onFailure] to handle
 * upstream [Failure]s and emit a replacement value downstream.
 */
public inline fun <V, C, R> Flow<Outcome<V, C>>.use(
    crossinline onSuccess: (value: V) -> R,
    crossinline onFailure: (failure: Failure<C>) -> R,
): Flow<R> = map { outcome -> outcome.use(onSuccess, onFailure) }

/**
 * Converts `this` [Flow] into a [Flow] of values returned by [onSuccess] if
 * the upstream [Outcome] is a [Success], or calls [onFailure] to handle
 * upstream [Failure]s.
 *
 * This is a more flexible overload, as [onFailure] may or may not emit values
 * downstream.
 *
 * @see transform
 */
@OptIn(ExperimentalTypeInference::class)
public inline fun <V, C, R> Flow<Outcome<V, C>>.use(
    crossinline onSuccess: (value: V) -> R,
    @BuilderInference
    crossinline onFailure: suspend FlowCollector<R>.(failure: Failure<C>) -> Unit,
): Flow<R> = transform { outcome ->
    when (outcome) {
        is Success -> emit(onSuccess(outcome.value))
        is Failure -> onFailure(outcome)
    }
}

/**
 * Converts `this` [Flow] into a [Flow] of the encapsulated
 * [value][Success.value]s if the upstream [Outcome] is a [Success], or calls
 * [onFailure] to handle upstream [Failure]s and emit `null` downstream.
 */
public inline fun <V, C> Flow<Outcome<V, C>>.useOrNull(
    crossinline onFailure: (failure: Failure<C>) -> Unit = {},
): Flow<V?> = map { outcome -> outcome.useOrNull(onFailure) }

/**
 * Converts `this` [Flow] into a [Flow] of values returned by [onSuccess] if
 * the upstream [Outcome] is a [Success], or calls [onFailure] to handle
 * upstream [Failure]s and emit `null` downstream.
 */
public inline fun <V, C, R> Flow<Outcome<V, C>>.useOrNull(
    crossinline onSuccess: (value: V) -> R,
    crossinline onFailure: (failure: Failure<C>) -> Unit = {},
): Flow<R?> = map { outcome -> outcome.useOrNull(onSuccess, onFailure) }
