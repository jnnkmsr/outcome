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

/**
 * Returns the encapsulated [value][Success.value] if `this` [Outcome] is a
 * [Success], or calls [onFailure] to handle a [Failure] and return a
 * replacement value.
 */
public inline fun <V, C> Outcome<V, C>.use(
    onFailure: (failure: Failure<C>) -> V,
): V = when (this) {
    is Success -> value
    is Failure -> onFailure(this)
}

/**
 * Returns the result of [onSuccess] if `this` [Outcome] is a [Success], or
 * calls [onFailure] to handle a [Failure] and return a replacement value.
 */
public inline fun <V, C, R> Outcome<V, C>.use(
    onSuccess: (value: V) -> R,
    onFailure: (failure: Failure<C>) -> R,
): R = when (this) {
    is Success -> onSuccess(value)
    is Failure -> onFailure(this)
}

/**
 * Returns the encapsulated [value][Success.value] if `this` [Outcome] is a
 * [Success], or calls [onFailure] to handle the [Failure] and returns `null`
 * instead.
 */
public inline fun <V, C> Outcome<V, C>.useOrNull(
    onFailure: (failure: Failure<C>) -> Unit = {},
): V? = when (this) {
    is Success -> value
    is Failure -> {
        onFailure(this)
        null
    }
}

/**
 * Returns the result of [onSuccess] if `this` [Outcome] is a [Success], or
 * [Success], or calls [onFailure] to handle the [Failure] and returns `null`
 * instead.
 */
public inline fun <V, C, R> Outcome<V, C>.useOrNull(
    onSuccess: (value: V) -> R,
    onFailure: (failure: Failure<C>) -> Unit = {},
): R? = when (this) {
    is Success -> onSuccess(value)
    is Failure -> {
        onFailure(this)
        null
    }
}
