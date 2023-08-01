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
 * Returns the [Outcome] of the given [block] if `this` previous [Outcome] was
 * a [Success], or returns `this` [Failure].
 */
public inline fun <V, C, R> Outcome<V, C>.use(
    block: (value: V) -> Outcome<R, C>,
): Outcome<R, C> = when (this) {
    is Success -> block(value)
    is Failure -> this
}
