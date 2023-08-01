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
 * Maps the encapsulated [value][Success.value] using the given [transform] if
 * `this` [Outcome] is a [Success], or returns `this` [Failure]
 */
public inline fun <V, R, C> Outcome<V, C>.map(
    transform: (value: V) -> R,
): Outcome<R, C> = use { value -> Success(transform(value)) }
