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
 * Invokes the given [block] if `this` [Outcome] is a [Success]. Then returns
 * `this` [Outcome] unchanged.
 */
public inline fun <V, C> Outcome<V, C>.onSuccess(
    block: (value: V) -> Unit,
): Outcome<V, C> = also { if (this is Success) block(value) }

/**
 * Invokes the given [block] if `this` [Outcome] is a [Failure]. Then returns
 * `this` [Outcome] unchanged.
 */
public inline fun <V, C> Outcome<V, C>.onFailure(
    block: (failure: Failure<C>) -> Unit,
): Outcome<V, C> = also { if (this is Failure) block(this) }
