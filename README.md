![JitPack](https://img.shields.io/jitpack/version/com.github.jnnkmsr/outcome?style=for-the-badge)
![GitHub](https://img.shields.io/github/license/jnnkmsr/outcome?style=for-the-badge)
![GitHub top language](https://img.shields.io/github/languages/top/jnnkmsr/outcome?style=for-the-badge)

# Outcome

A library for type-safe error handling in Kotlin.

## Download

Add the [JitPack][jitpack] repository and the library dependency to your Gradle
build scripts.

### Kotlin DSL

```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.jnnkmsr.outcome:outcome:<version>")
}
```

### Groovy DSL

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.jnnkmsr.outcome:outcome:<version>'
}
```

## License

```
Copyright 2023 Jannik Möser

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

<!-- External Links -->
[jitpack]: https://jitpack.io/
[state-events]: https://github.com/leonard-palm/compose-state-events/tree/master
[state-events-article]: https://proandroiddev.com/how-to-handle-viewmodel-one-time-events-in-jetpack-compose-a01af0678b76